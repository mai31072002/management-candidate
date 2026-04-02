package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.PageDataRep;
import com.candidate.candidate_backend.dto.user.DtoCreateUserEmployee;
import com.candidate.candidate_backend.dto.user.DtoUserRep;
import com.candidate.candidate_backend.dto.user.DtoUserReq;
import com.candidate.candidate_backend.entity.*;
import com.candidate.candidate_backend.exception.BusinessException;
import com.candidate.candidate_backend.mapper.EmployeeMapper;
import com.candidate.candidate_backend.mapper.UserMapper;
import com.candidate.candidate_backend.repositorry.DepartmentRepository;
import com.candidate.candidate_backend.repositorry.PositionRepository;
import com.candidate.candidate_backend.repositorry.RoleRepository;
import com.candidate.candidate_backend.repositorry.UserRepository;
import com.candidate.candidate_backend.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.candidate.candidate_backend.util.Helper.isAdmin;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    public CommonsRep createUserEmployee(DtoCreateUserEmployee dtoCreateUserEmployee) {

        ValidateBusiness(dtoCreateUserEmployee);

        // Map từ DTO sang Entity
        DtbUser user = UserMapper.toEntityUser(dtoCreateUserEmployee);

        if (dtoCreateUserEmployee.getRoles().contains("ADMIN") && !isAdmin()) {
            throw new BusinessException(HttpStatus.FORBIDDEN, "Không có quyền gán role ADMIN");
        }

        String username = dtoCreateUserEmployee.getUsername();

        String formattedPassword =
                username.substring(0, 1).toUpperCase()
                + username.substring(1)
                + "@123";

        user.setPassword(passwordEncoder.encode(formattedPassword));

        // encode password
        user.setPassword(passwordEncoder.encode(formattedPassword));

        // map roles => entity
        Set<DtbRole> roleEntities = dtoCreateUserEmployee.getRoles()
                .stream()
                .map(roleName -> roleRepository.findByRoleName(roleName)
                        .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST,
                                "Role not found: " + roleName)))
                .collect(Collectors.toSet());

        user.setRoles(roleEntities);

        DtbPosition dtbPosition = positionRepository.findByIdAndIsDeletedFalse(dtoCreateUserEmployee.getPositionId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Chức vụ Không được để trống"));

        DtbDepartment dtbDepartment = departmentRepository.findByIdAndIsDeletedFalse(dtoCreateUserEmployee.getDepartmentId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Phòng ban Không được để trống"));

        DtbEmployees dtbEmployees = EmployeeMapper.toEntityEmployee(dtoCreateUserEmployee, dtbPosition, dtbDepartment);

        dtbEmployees.setUser(user);

        user.setEmployee(dtbEmployees);

        try {
            // save vào DB
            DtbUser saved = userRepository.save(user);

            // map Entity sang Dto
            DtoUserRep response = UserMapper.toDto(saved);

            return Helper.getServerResponse(HttpStatus.OK, "Thêm User Thành công", response);
        } catch (Exception e) {
            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
        }
    }

    public CommonsRep getUsers(int page, int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("updateAt").descending()
        );

        Page<DtbUser> dtbUsers = userRepository.findAllByIsDeletedFalse(pageable);

        PageDataRep<DtoUserRep> pageData = new PageDataRep<>();
        pageData.setData(
                dtbUsers.getContent()
                        .stream()
                        .map(UserMapper::toDto)
                        .toList()
        );

        pageData.setPageableRep(Helper.mapToPageableRep(dtbUsers));

        return Helper.getServerResponse(HttpStatus.OK, "Lấy ra danh sách user Thành công", pageData);
    }

//    @Cacheable(value = "dtb_user", key = "userId")
    public CommonsRep getUserById(String userId) {
        UUID uuidUserId = UUID.fromString(userId);

        DtbUser userEntity = userRepository.findById(uuidUserId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "User not found"));

        // Map entity → DTO
        DtoUserRep getUser = UserMapper.toDto(userEntity);

        return Helper.getServerResponse(HttpStatus.OK, "Lấy chi tiết user Thành công", getUser);
    }

    public CommonsRep getInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        try {
            DtbUser getInfo = userRepository.findByUsernameAndIsDeletedFalse(name)
                                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not exist"));
            return Helper.getServerResponse(HttpStatus.OK, "Lấy chi tiết user Thành công", getInfo);
        } catch (ResponseStatusException e) {
            return Helper.getServerResponse(HttpStatus.NOT_FOUND, "User not exist", null);
        }
    }

    @CachePut(value = "dtb_user", key = "userId")
    public CommonsRep updateUser(String userId, DtoUserReq dtoUserReq) {
        UUID uuidUserId = UUID.fromString(userId);
        DtbUser user = userRepository.findById(uuidUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id not exist"));

        boolean roleChanged = dtoUserReq.getRoles() != null && hasRoleChanged(user, dtoUserReq);
        boolean employeeChanged = hasEmployeeChanged(user, dtoUserReq);

        if (!hasChangeUser(user, dtoUserReq) && !roleChanged && !employeeChanged) {
            return Helper.getServerResponse(HttpStatus.OK, "Không có thay đổi user", null);
        }

        // Update User info
        if (hasChangeUser(user, dtoUserReq)) {
            user.setUsername(dtoUserReq.getUsername());
            user.setEmail(dtoUserReq.getEmail());
        }

        // Update Roles
        if (roleChanged) {
            Set<DtbRole> roleEntities = dtoUserReq.getRoles()
                    .stream()
                    .map(roleName -> roleRepository.findByRoleName(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
            user.setRoles(roleEntities);
        }

        // Update Employee info
        if (employeeChanged) {
            if (user.getEmployee() == null) {
                // Create new employee if not exists
                DtbEmployees employee = new DtbEmployees();
                updateEmployeeFromDto(employee, dtoUserReq);
                user.setEmployee(employee);
            } else {
                // Update existing employee
                updateEmployeeFromDto(user.getEmployee(), dtoUserReq);
            }
        }

        DtbUser saved = userRepository.save(user);
        DtoUserRep response = UserMapper.toDto(saved);

        return Helper.getServerResponse(HttpStatus.OK, "Cập nhật User Thành công", response);
    }
//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @CacheEvict(value = "dtb_user", key = "userId")
    public CommonsRep deleteUser(String userId) {
        try {
            UUID uuidUserId = UUID.fromString(userId);
            DtbUser dtbUser = userRepository.findById(uuidUserId)
                            .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "userId không hợp lệ"));

            // Soft delete User
            dtbUser.setDeleted(true);
            
            // Soft delete Employee if exists
            if (dtbUser.getEmployee() != null) {
                dtbUser.getEmployee().setDeleted(true);
            }
            
            userRepository.save(dtbUser);

            return Helper.getServerResponse(HttpStatus.OK, "Xóa User Thành công", null);
        } catch (IllegalArgumentException e) {
            return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "UUID Không hợp lệ", null);
        }
    }

//
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CommonsRep searchUsername(String keyword, int page, int size) {

        Pageable pageable = PageRequest.of(
                    page,
                    size
//                    Sort.by("updateAt").descending()
            );

            Page<DtbUser> searchData = userRepository.searchByUsernameIgnoreCaseAndAccent(keyword, pageable);

            PageDataRep<DtoUserRep> pageData = new PageDataRep<>();
            pageData.setData(
                    searchData.getContent()
                            .stream()
                            .map(UserMapper::toDto)
                            .toList()
            );

            pageData.setPageableRep(Helper.mapToPageableRep(searchData));

        return Helper.getServerResponse(HttpStatus.OK, "Lấy dữ liệu thành công", pageData);
    }


    public boolean hasChangeUser (DtbUser dtbUser, DtoUserReq dtoUserReq) {
        return !Objects.equals(dtbUser.getUsername(), dtoUserReq.getUsername()) ||
//            !Objects.equals(dtbUser.getFirstName(), dtoUserReq.getFirstName()) ||
//            !Objects.equals(dtbUser.getLastName(), dtoUserReq.getLastName()) ||
            !Objects.equals(dtbUser.getEmail(), dtoUserReq.getEmail());
    }

    private boolean hasRoleChanged(DtbUser user, DtoUserReq dtoUserReq) {
        Set<String> currentRoles = user.getRoles()
                .stream()
                .map(DtbRole::getRoleName)
                .collect(Collectors.toSet());

        Set<String> requestRoles = new HashSet<>(dtoUserReq.getRoles());

        return !currentRoles.equals(requestRoles);
    }

    private void ValidateBusiness(DtoCreateUserEmployee dtoCreateUserEmployee) {
        if (userRepository.existsByUsername(dtoCreateUserEmployee.getUsername())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "username đã tồn tại");
        }

        if (userRepository.existsByEmail(dtoCreateUserEmployee.getEmail())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "email đã tồn tại");
        }

        if (dtoCreateUserEmployee.getEndDate() != null &&
                dtoCreateUserEmployee.getEndDate().isBefore(dtoCreateUserEmployee.getStartDate())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Ngày không hợp lệ");
        }
    }

    private boolean hasEmployeeChanged(DtbUser user, DtoUserReq dtoUserReq) {
        if (user.getEmployee() == null) {
            // Check if any employee field is provided
            return dtoUserReq.getFirstName() != null || dtoUserReq.getLastName() != null ||
                   dtoUserReq.getPhone() != null || dtoUserReq.getAddress() != null ||
                   dtoUserReq.getBaseSalary() != null || dtoUserReq.getPositionId() != null ||
                   dtoUserReq.getDepartmentId() != null;
        }
        
        DtbEmployees employee = user.getEmployee();
        return !Objects.equals(employee.getFirstName(), dtoUserReq.getFirstName()) ||
               !Objects.equals(employee.getLastName(), dtoUserReq.getLastName()) ||
               !Objects.equals(employee.getPhone(), dtoUserReq.getPhone()) ||
               !Objects.equals(employee.getAddress(), dtoUserReq.getAddress()) ||
               !Objects.equals(employee.getBaseSalary(), dtoUserReq.getBaseSalary()) ||
               !Objects.equals(employee.getPosition().getId(), dtoUserReq.getPositionId()) ||
               !Objects.equals(employee.getDepartment().getId(), dtoUserReq.getDepartmentId());
    }

    private void updateEmployeeFromDto(DtbEmployees employee, DtoUserReq dtoUserReq) {
        if (dtoUserReq.getFirstName() != null) {
            employee.setFirstName(dtoUserReq.getFirstName());
        }
        if (dtoUserReq.getLastName() != null) {
            employee.setLastName(dtoUserReq.getLastName());
        }
        if (dtoUserReq.getPhone() != null) {
            employee.setPhone(dtoUserReq.getPhone());
        }
        if (dtoUserReq.getAddress() != null) {
            employee.setAddress(dtoUserReq.getAddress());
        }
        if (dtoUserReq.getBaseSalary() != null) {
            employee.setBaseSalary(dtoUserReq.getBaseSalary());
        }
        if (dtoUserReq.getPositionId() != null) {
            DtbPosition position = positionRepository.findByIdAndIsDeletedFalse(dtoUserReq.getPositionId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Chức vụ không tồn tại"));
            employee.setPosition(position);
        }
        if (dtoUserReq.getDepartmentId() != null) {
            DtbDepartment department = departmentRepository.findByIdAndIsDeletedFalse(dtoUserReq.getDepartmentId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Phòng ban không tồn tại"));
            employee.setDepartment(department);
        }
    }

    public CommonsRep importExcel(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "File không được để trống", null);
            }

            if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
                return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "Chỉ chấp nhận file Excel (.xlsx, .xls)", null);
            }

            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            List<String> errors = new ArrayList<>();
            List<DtoCreateUserEmployee> validEmployees = new ArrayList<>();
            int rowIndex = 1; // Bỏ qua header

            for (Row row : sheet) {
                if (rowIndex == 1) { // Skip header row
                    rowIndex++;
                    continue;
                }

                try {
                    DtoCreateUserEmployee employee = parseRowToEmployee(row, rowIndex);
                    validateEmployeeData(employee, rowIndex, validEmployees);
                    validEmployees.add(employee);
                } catch (Exception e) {
                    errors.add("Dòng " + rowIndex + ": " + e.getMessage());
                }
                rowIndex++;
            }

            workbook.close();

            if (!errors.isEmpty()) {
                return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "Lỗi validation: " + String.join(", ", errors), null);
            }

            // Save all valid employees
            List<String> successMessages = new ArrayList<>();
            for (DtoCreateUserEmployee employee : validEmployees) {
                try {
                    createUserEmployee(employee);
                    successMessages.add("Thêm thành công: " + employee.getUsername());
                } catch (Exception e) {
                    errors.add("Lỗi khi thêm " + employee.getUsername() + ": " + e.getMessage());
                }
            }

            if (!errors.isEmpty()) {
                return Helper.getServerResponse(HttpStatus.PARTIAL_CONTENT, 
                    "Hoàn thành " + successMessages.size() + " records, lỗi " + errors.size() + " records. " + 
                    "Lỗi: " + String.join(", ", errors), null);
            }

            return Helper.getServerResponse(HttpStatus.OK, "Import thành công " + successMessages.size() + " nhân viên", null);

        } catch (Exception e) {
            log.error("Error importing Excel", e);
            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi import file: " + e.getMessage(), null);
        }
    }

    private DtoCreateUserEmployee parseRowToEmployee(Row row, int rowIndex) throws Exception {
        DtoCreateUserEmployee employee = new DtoCreateUserEmployee();
        
        employee.setFirstName(getCellValue(row, 0));
        employee.setLastName(getCellValue(row, 1));
        employee.setUsername(getCellValue(row, 2));
        employee.setEmail(getCellValue(row, 3));
        employee.setPhone(getCellValue(row, 4));
        employee.setCccd(getCellValue(row, 5));
        employee.setAddress(getCellValue(row, 6));
        employee.setProvince(getCellValue(row, 7));
        employee.setDistrict(getCellValue(row, 8));
        employee.setGender(getCellValue(row, 9).equals("Nam") ? 1 : 0);
        employee.setBirthday(getCellDateAsLocalDate(row.getCell(10)));
        employee.setStartDate(getCellDateAsLocalDate(row.getCell(11)));
        employee.setEmployeesCode(getCellValue(row, 12));
        employee.setBaseSalary(new java.math.BigDecimal(getCellValue(row, 13)));
        employee.setAllowance(new java.math.BigDecimal(getCellValue(row, 14)));
        employee.setManages(getCellValue(row, 15));
        employee.setDescription(getCellValue(row, 16));
        
        // Set position and department IDs (cần lookup từ tên)
        String positionName = getCellValue(row, 17);
        String departmentName = getCellValue(row, 18);
        
        employee.setPositionId(findPositionIdByName(positionName));
        employee.setDepartmentId(findDepartmentIdByName(departmentName));
        
//        employee.setRoles("USER"); // Default role
//        employee.setRoles();
        
        return employee;
    }

    private LocalDate getCellDateAsLocalDate(Cell cell) {
    if (cell == null) return null;

    if (cell.getCellType() == CellType.NUMERIC) {
        if (DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else {
            // Nếu cell là số nhưng không phải ngày, convert Excel serial -> LocalDate
            double excelDate = cell.getNumericCellValue();
            return DateUtil.getJavaDate(excelDate).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        }
    } else if (cell.getCellType() == CellType.STRING) {
        String text = cell.getStringCellValue();
        try {
            return LocalDate.parse(text); // String dạng "yyyy-MM-dd"
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Ngày không hợp lệ: " + text);
        }
    } else {
        throw new RuntimeException("Cell không chứa ngày hợp lệ");
    }
}

    private String getCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    private UUID findPositionIdByName(String positionName) throws Exception {
        return positionRepository.findByPositionNameAndIsDeletedFalse(positionName)
                .orElseThrow(() -> new Exception("Không tìm thấy chức vụ: " + positionName))
                .getId();
    }

    private UUID findDepartmentIdByName(String departmentName) throws Exception {
        return departmentRepository.findByDepartmentNameAndIsDeletedFalse(departmentName)
                .orElseThrow(() -> new Exception("Không tìm thấy phòng ban: " + departmentName))
                .getId();
    }

    private void validateEmployeeData(DtoCreateUserEmployee employee, int rowIndex, List<DtoCreateUserEmployee> existingEmployees) throws Exception {
        if (employee.getUsername().isEmpty()) {
            throw new Exception("Username không được để trống");
        }
        if (employee.getEmail().isEmpty()) {
            throw new Exception("Email không được để trống");
        }
        
        // Check duplicates
        if (userRepository.existsByUsername(employee.getUsername())) {
            throw new Exception("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(employee.getEmail())) {
            throw new Exception("Email đã tồn tại");
        }
        
        // Check in current import list
        for (DtoCreateUserEmployee existing : existingEmployees) {
            if (existing.getUsername().equals(employee.getUsername())) {
                throw new Exception("Username trùng lặp trong file import");
            }
            if (existing.getEmail().equals(employee.getEmail())) {
                throw new Exception("Email trùng lặp trong file import");
            }
            if (existing.getEmployeesCode().equals(employee.getEmployeesCode())) {
                throw new Exception("Mã nhân viên trùng lặp trong file import");
            }
        }
    }

    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Employee Template");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "FirstName", "LastName", "Username", "Email", "Phone", "CCCD", 
                "Address", "Province", "District", "Gender", "Birthday", "StartDate",
                "EmployeeCode", "BaseSalary", "Allowance", "Manager", "Description",
                "PositionName", "DepartmentName"
            };
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to output stream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            
            byte[] bytes = outputStream.toByteArray();
            
            HttpHeaders headers1 = new HttpHeaders();
            headers1.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers1.setContentDispositionFormData("attachment", "employee-template.xlsx");
            headers1.setContentLength(bytes.length);
            
            return ResponseEntity.ok()
                    .headers(headers1)
                    .body(bytes);
                    
        } catch (Exception e) {
            log.error("Error generating template", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}

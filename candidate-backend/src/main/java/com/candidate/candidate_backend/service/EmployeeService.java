package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.PageDataRep;
import com.candidate.candidate_backend.dto.employee.DtoEmployeeRep;
import com.candidate.candidate_backend.dto.employee.DtoEmployeeReq;
import com.candidate.candidate_backend.entity.*;
import com.candidate.candidate_backend.exception.BusinessException;
import com.candidate.candidate_backend.mapper.EmployeeMapper;
import com.candidate.candidate_backend.repositorry.*;
import com.candidate.candidate_backend.util.Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional
    public CommonsRep createEmployee(DtoEmployeeReq dtoEmployeeReq) {

        Optional<DtbEmployees> existEmployee =
            employeeRepository.findByEmployeesCodeAndIsDeletedFalse(
//                        dtoEmployeeReq.getUsername(),
                    dtoEmployeeReq.getEmployeesCode()
            );

        if (existEmployee.isPresent()) {
            DtbEmployees e = existEmployee.get();
//            if (e.getUsername().equals(dtoEmployeeReq.getUsername())) {
//                return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "Username đã tồn tại", null);
//            }
            if (e.getEmployeesCode().equals(dtoEmployeeReq.getEmployeesCode())) {
                return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "Mã nhân viên đã tồn tại", null);
            }
        }

        DtbPosition dtbPosition = positionRepository.findByIdAndIsDeletedFalse(dtoEmployeeReq.getPositionId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Chức vụ Không được để trống"));

        DtbDepartment dtbDepartment = departmentRepository.findByIdAndIsDeletedFalse(dtoEmployeeReq.getDepartmentId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Phòng ban Không được để trống"));

        DtbEmployees dtbEmployees = EmployeeMapper.toEntity(dtoEmployeeReq, dtbPosition, dtbDepartment);

//        DtbUser user = new DtbUser();
////            user.setFirstName(dtoEmployeeReq.getFirstName());
////            user.setLastName(dtoEmployeeReq.getLastName());
//        user.setUsername(dtoEmployeeReq.getUsername());
//        user.setEmail(dtoEmployeeReq.getEmail());
//        user.setPassword(passwordEncoder.encode("123456")); // hoặc random
//        user.setEmployee(dtbEmployees);
//
//        // role cố định USER
//        DtbRole userRole = roleRepository.findByRoleName("USER")
//                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "ROLE_USER không tồn tại"));
//
//        user.setRoles(Set.of(userRole));

        // gán 2 chiều
//        dtbEmployees.setUser(user);


        DtbEmployees save = employeeRepository.save(dtbEmployees);

        DtoEmployeeRep dtoEmployeeRep = EmployeeMapper.toDto(save);

        return Helper.getServerResponse(HttpStatus.OK, "Tạo mới nhân viên thành công", dtoEmployeeRep);
    }

    @Transactional
    public CommonsRep getByEmployee(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(
                    page,
                    size,
                    Sort.by("updateAt").descending()
            );

            Page<DtbEmployees> dtbEmployees = employeeRepository.findAllByIsDeletedFalse(pageable);

            PageDataRep<DtoEmployeeRep> pageData = new PageDataRep<>();
            pageData.setData(
                    dtbEmployees.getContent()
                            .stream()
                            .map(EmployeeMapper::toDto)
                            .toList()
            );

            pageData.setPageableRep(Helper.mapToPageableRep(dtbEmployees));

            return Helper.getServerResponse(HttpStatus.OK, "Lấy ra danh sách nhân viên thành công", pageData);
        } catch (Exception e) {
            log.warn("Lỗi khi lấy danh sách nhân viên: ", e);
            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
        }
    }

    public CommonsRep searchFullName(String keyword, int page, int size) {
        try {
            Pageable pageable = PageRequest.of(
                    page,
                    size
//                    Sort.by("updateAt").descending()
            );

            Page<DtbEmployees> dtbEmployees =
                    employeeRepository.searchByFullNameIgnoreCaseAndAccent(keyword, pageable);

            PageDataRep<DtoEmployeeRep> pageData = new PageDataRep<>();
            pageData.setData(
                    dtbEmployees.getContent()
                            .stream()
                            .map(EmployeeMapper::toDto)
                            .toList()
            );

            pageData.setPageableRep(Helper.mapToPageableRep(dtbEmployees));

            return Helper.getServerResponse(HttpStatus.OK, "Tìm kiếm thành công", pageData);
        } catch (Exception e) {
            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
        }
    }

    public CommonsRep getEmployeeById(String employeeId) {
        try {
            UUID uuidEmployeeId = UUID.fromString(employeeId);

            DtbEmployees dtbEmployees = employeeRepository.findByIdAndIsDeletedFalse(uuidEmployeeId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Nhân viên không tồn tại"));

            DtoEmployeeRep dtoEmployeeRep = EmployeeMapper.toDto(dtbEmployees);

            return Helper.getServerResponse(HttpStatus.OK, "Lấy ra chi tiết nhân viên thành công", dtoEmployeeRep);
        } catch (Exception e) {
            log.warn("Lỗi lấy chi tiết nhân viên: ", e);
            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
        }
    }

    public CommonsRep deleteEmployee(String employeeId) {
        try {
            UUID uuidEmployeeId = UUID.fromString(employeeId);

            DtbEmployees dtbEmployees = employeeRepository.findByIdAndIsDeletedFalse(uuidEmployeeId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Nhân viên không tồn tại"));

            dtbEmployees.setDeleted(true);

            employeeRepository.save(dtbEmployees);

            return Helper.getServerResponse(HttpStatus.OK, "Xóa nhân viên thành công", null);
        } catch (Exception e) {
            log.warn("Lỗi xóa nhân viên: ", e);
            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
        }
    }

    @Transactional
    public CommonsRep updateEmployee(String employeeId, DtoEmployeeReq dtoEmployeeReq) {
        try {
            UUID uuidEmployeeId = UUID.fromString(employeeId);

            DtbEmployees dtbEmployees = employeeRepository.findByIdAndIsDeletedFalse(uuidEmployeeId)
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Nhân viên không tồn tại"));

            if (dtoEmployeeReq.getStatus() == 2) {
                if (dtoEmployeeReq.getEndDate() == null) {
                    throw new BusinessException(
                        HttpStatus.BAD_REQUEST,
                        "Nhân viên đã nghỉ việc thì bắt buộc phải có ngày nghỉ (endDate)"
                    );
                }

                if (dtoEmployeeReq.getStartDate() != null &&
                    dtoEmployeeReq.getEndDate().isBefore(dtoEmployeeReq.getStartDate())) {
                    throw new BusinessException(
                        HttpStatus.BAD_REQUEST,
                        "Ngày nghỉ việc (endDate) phải lớn hơn ngày vào làm (startDate)"
                    );
                }
            }

            DtbPosition dtbPosition = positionRepository.findByIdAndIsDeletedFalse(dtoEmployeeReq.getPositionId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Chức vụ không tồn tại"));

            DtbDepartment dtbDepartment = departmentRepository.findByIdAndIsDeletedFalse(dtoEmployeeReq.getDepartmentId())
                    .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Phòng ban không tồn tại"));

            if (!hasChangeEmployee(dtbEmployees, dtbPosition, dtoEmployeeReq)) {
                return Helper.getServerResponse(HttpStatus.OK, "Không có sự thay đổi", null);
            }

            dtbEmployees.setFirstName(dtoEmployeeReq.getFirstName());
            dtbEmployees.setLastName(dtoEmployeeReq.getLastName());
            dtbEmployees.setFullName(dtoEmployeeReq.getFirstName() + dtoEmployeeReq.getLastName());
//            dtbEmployees.setUsername(dtoEmployeeReq.getUsername());
            dtbEmployees.setProvince(dtoEmployeeReq.getProvince());
            dtbEmployees.setDistrict(dtoEmployeeReq.getDistrict());
            dtbEmployees.setAddress(dtoEmployeeReq.getAddress());
            dtbEmployees.setGender(dtoEmployeeReq.getGender());
            dtbEmployees.setBirthday(dtoEmployeeReq.getBirthday());
            dtbEmployees.setEmail(dtoEmployeeReq.getEmail());
            dtbEmployees.setDescription(dtoEmployeeReq.getDescription());
            dtbEmployees.setStartDate(dtoEmployeeReq.getStartDate());
            dtbEmployees.setEndDate(dtoEmployeeReq.getEndDate());
            dtbEmployees.setStatus(dtoEmployeeReq.getStatus());
            dtbEmployees.setPosition(dtbPosition);
            dtbEmployees.setDepartment(dtbDepartment);
            //        dtbEmployees.setDepartment(dtbDepartment);
            dtbEmployees.setEmployeesCode(dtoEmployeeReq.getEmployeesCode());
            dtbEmployees.setManages(dtoEmployeeReq.getManages());
            dtbEmployees.setAllowance(dtoEmployeeReq.getAllowance());
            dtbEmployees.setBaseSalary(dtoEmployeeReq.getBaseSalary());

            DtbUser user = dtbEmployees.getUser();
            if (user != null) {
//                user.setFirstName(dtoEmployeeReq.getFirstName());
//                user.setLastName(dtoEmployeeReq.getLastName());
//                user.setUsername(dtoEmployeeReq.getUsername());
                user.setEmail(dtoEmployeeReq.getEmail());
                // username có thể không update được nếu là unique
            }

            DtbEmployees save = employeeRepository.save(dtbEmployees);

            DtoEmployeeRep dtoEmployeeRep = EmployeeMapper.toDto(save);

            return Helper.getServerResponse(HttpStatus.OK, "Cập nhật nhân viên thành công", dtoEmployeeRep);
        } catch (Exception e) {
            log.warn("Lỗi cập nhật nhân viên: ", e);
            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống", null);
        }
    }

    private boolean hasChangeEmployee(DtbEmployees dtbEmployees, DtbPosition dtbPosition, DtoEmployeeReq dtoEmployeeReq                                ) {
        boolean sameName =
            Objects.equals(dtbEmployees.getFirstName(), dtoEmployeeReq.getFirstName())
         && Objects.equals(dtbEmployees.getLastName(), dtoEmployeeReq.getLastName());

//        boolean sameUsername =
//                Objects.equals(dtbEmployees.getUsername(), dtoEmployeeReq.getUsername());

        boolean sameAddress =
                Objects.equals(dtbEmployees.getProvince(), dtoEmployeeReq.getProvince())
             && Objects.equals(dtbEmployees.getDistrict(), dtoEmployeeReq.getDistrict())
             && Objects.equals(dtbEmployees.getAddress(), dtoEmployeeReq.getAddress());

        boolean sameBirthday =
                Objects.equals(dtbEmployees.getBirthday(), dtoEmployeeReq.getBirthday());

        boolean sameEmail =
                Objects.equals(dtbEmployees.getEmail(), dtoEmployeeReq.getEmail());

        boolean sameStatus =
                Objects.equals(dtbEmployees.getStatus(), dtoEmployeeReq.getStatus());

        boolean samePosition =
                dtoEmployeeReq.getPositionId() != null
                && dtbEmployees.getPosition().getId().equals(dtoEmployeeReq.getPositionId());

//        boolean sameDepartment =
//                dtbEmployees.getPosition() != null
//                && dtbEmployees.getPosition().getDepartment() != null
//                && dtbEmployees.getPosition().getDepartment().getId()
//                    .equals(dtoEmployeeReq.getDepartmentId());

        return sameName
//            && sameUsername
            && sameAddress
            && sameBirthday
            && sameEmail
            && sameStatus
            && samePosition;
//            && sameDepartment;
    }
}

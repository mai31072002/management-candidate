package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.department.DtoDepartmentRep;
import com.candidate.candidate_backend.dto.department.DtoDepartmentReq;
import com.candidate.candidate_backend.entity.DtbDepartment;
import com.candidate.candidate_backend.exception.BusinessException;
import com.candidate.candidate_backend.mapper.DepartmentMapper;
import com.candidate.candidate_backend.repositorry.DepartmentRepository;
import com.candidate.candidate_backend.repositorry.EmployeeRepository;
import com.candidate.candidate_backend.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public CommonsRep createDepartment(DtoDepartmentReq dtoDepartmentReq) {
        if (departmentRepository.existsByDepartmentName(dtoDepartmentReq.getDepartmentName())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "Phòng ban đã tồn tại");
        }

        // Map từ DTO sang Entity
        DtbDepartment dtbDepartment = DepartmentMapper.toEntity(dtoDepartmentReq);

        // save vào DB
        DtbDepartment saved = departmentRepository.save(dtbDepartment);

        // map Entity sang Dto
        DtoDepartmentRep response = DepartmentMapper.toDto(saved);

        return Helper.getServerResponse(HttpStatus.OK, "Thêm phòng ban Thành công", response);
    }

    public CommonsRep getDepartment() {
        List<DtbDepartment> dtbDepartment = departmentRepository.findAllByIsDeletedFalse();

        List<DtoDepartmentRep> dtoDepartmentRep =
                dtbDepartment
                        .stream()
                        .map(DepartmentMapper::toDto)
                        .toList()
                ;
        return Helper.getServerResponse(HttpStatus.OK, "Lấy ra danh sách phòng ban thành công", dtoDepartmentRep);
    }

    public CommonsRep getDepartmentById(String departmentId) {
        UUID uuidDepartmentId = UUID.fromString(departmentId);

        DtbDepartment dtbDepartment = departmentRepository.findByIdAndIsDeletedFalse(uuidDepartmentId)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Phòng ban Không tồn tại"));

        DtoDepartmentRep dtoDepartmentRep = DepartmentMapper.toDto(dtbDepartment);

        return Helper.getServerResponse(HttpStatus.OK, "Lấy chi tiết phòng ban thành công", dtoDepartmentRep);
    }

    public CommonsRep deleteDepartment(String departmentId) {
        UUID uuidDepartmentId = UUID.fromString(departmentId);

        long count = employeeRepository.countByDepartmentIdAndIsDeletedFalse(uuidDepartmentId);

        if (count > 0) {
            return Helper.getServerResponse(HttpStatus.BAD_GATEWAY, "Không thể xóa phòng ban vì vẫn còn nhân " +
                    "viên", null);
        }

        DtbDepartment dtbDepartment = departmentRepository.findById(uuidDepartmentId)
                        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Phòng ban không tồn tại"));

        dtbDepartment.setDeleted(true);
        departmentRepository.save(dtbDepartment);

        return Helper.getServerResponse(HttpStatus.OK, "xóa phòng ban thành công", null);
    }

    public CommonsRep updateDepartment(String departmentId, DtoDepartmentReq dtoDepartmentReq) {
        UUID uuidDepartmentId = UUID.fromString(departmentId);

        DtbDepartment dtbDepartment = departmentRepository.findById(uuidDepartmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Phòng ban Không tồn tại", null));

        if (!hasChangeDepartment(dtbDepartment, dtoDepartmentReq)) {
            return Helper.getServerResponse(HttpStatus.OK, "Không có thay đổi", null);
        }

        dtbDepartment.setDepartmentName(dtoDepartmentReq.getDepartmentName());
        dtbDepartment.setDescription(dtoDepartmentReq.getDescription());

        DtbDepartment save = departmentRepository.save(dtbDepartment);

        DtoDepartmentRep dtoDepartmentRep = DepartmentMapper.toDto(save);

        return Helper.getServerResponse(HttpStatus.OK, "cập nhật thành công", dtoDepartmentRep);

    }

    public boolean hasChangeDepartment(DtbDepartment dtbDepartment, DtoDepartmentReq dtoDepartmentReq) {
        return !Objects.equals(dtbDepartment.getDepartmentName(), dtoDepartmentReq.getDepartmentName()) ||
                !Objects.equals(dtbDepartment.getDescription(), dtoDepartmentReq.getDescription());
    }
}

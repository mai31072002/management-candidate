package com.candidate.candidate_backend.mapper;


import com.candidate.candidate_backend.dto.department.DtoDepartmentRep;
import com.candidate.candidate_backend.dto.department.DtoDepartmentReq;
import com.candidate.candidate_backend.entity.DtbDepartment;

public class DepartmentMapper {
    public static DtbDepartment toEntity(DtoDepartmentReq dto) {
        DtbDepartment department = new DtbDepartment();

//        department.setId(dto.getId());
        department.setDepartmentName(dto.getDepartmentName());
        department.setDescription(dto.getDescription());

        return department;
    }

    // Entity sang dto
    public static DtoDepartmentRep toDto(DtbDepartment dtbDepartment) {
        DtoDepartmentRep dto = new DtoDepartmentRep();

        dto.setId(dtbDepartment.getId());
        dto.setDepartmentName(dtbDepartment.getDepartmentName());
        dto.setDescription(dtbDepartment.getDescription());

        return dto;
    }
}

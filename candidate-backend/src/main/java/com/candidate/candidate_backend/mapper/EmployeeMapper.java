package com.candidate.candidate_backend.mapper;

import com.candidate.candidate_backend.dto.employee.DtoEmployeeRep;
import com.candidate.candidate_backend.dto.employee.DtoEmployeeReq;
import com.candidate.candidate_backend.dto.user.DtoCreateUserEmployee;
import com.candidate.candidate_backend.entity.DtbDepartment;
import com.candidate.candidate_backend.entity.DtbEmployees;
import com.candidate.candidate_backend.entity.DtbPosition;

public class EmployeeMapper {
    public static DtbEmployees toEntity(DtoEmployeeReq dto, DtbPosition dtbPosition, DtbDepartment dtbDepartment) {
        DtbEmployees dtbEmployees = new DtbEmployees();

        dtbEmployees.setFirstName(dto.getFirstName());
        dtbEmployees.setLastName(dto.getLastName());
        dtbEmployees.setFullName(dto.getFirstName() + dto.getLastName());
//        dtbEmployees.setUsername(dto.getUsername());
        dtbEmployees.setProvince(dto.getProvince());
        dtbEmployees.setDistrict(dto.getDistrict());
        dtbEmployees.setAddress(dto.getAddress());
        dtbEmployees.setGender(dto.getGender());
        dtbEmployees.setBirthday(dto.getBirthday());
        dtbEmployees.setEmail(dto.getEmail());
        dtbEmployees.setPhone(dto.getPhone());
        dtbEmployees.setCccd(dto.getCccd());
        dtbEmployees.setDescription(dto.getDescription());
        dtbEmployees.setStartDate(dto.getStartDate());
        dtbEmployees.setEndDate(dto.getEndDate());
        dtbEmployees.setStatus(dto.getStatus());
        dtbEmployees.setPosition(dtbPosition);
        dtbEmployees.setDepartment(dtbDepartment);
//        dtbEmployees.setDepartment(dtbDepartment);
        dtbEmployees.setEmployeesCode(dto.getEmployeesCode());
        dtbEmployees.setManages(dto.getManages());
        dtbEmployees.setAllowance(dto.getAllowance());
        dtbEmployees.setBaseSalary(dto.getBaseSalary());

        return dtbEmployees;
    }

    public static DtbEmployees toEntityEmployee(DtoCreateUserEmployee dtoCreateUserEmployee, DtbPosition dtbPosition, DtbDepartment dtbDepartment) {
        DtbEmployees dtbEmployees = new DtbEmployees();

        dtbEmployees.setFirstName(dtoCreateUserEmployee.getFirstName());
        dtbEmployees.setLastName(dtoCreateUserEmployee.getLastName());
        dtbEmployees.setFullName(dtoCreateUserEmployee.getFirstName() + dtoCreateUserEmployee.getLastName());
//        dtbEmployees.setUsername(dtoCreateUserEmployee.getUsername());
        dtbEmployees.setProvince(dtoCreateUserEmployee.getProvince());
        dtbEmployees.setDistrict(dtoCreateUserEmployee.getDistrict());
        dtbEmployees.setAddress(dtoCreateUserEmployee.getAddress());
        dtbEmployees.setGender(dtoCreateUserEmployee.getGender());
        dtbEmployees.setBirthday(dtoCreateUserEmployee.getBirthday());
        dtbEmployees.setEmail(dtoCreateUserEmployee.getEmail());
        dtbEmployees.setPhone(dtoCreateUserEmployee.getPhone());
        dtbEmployees.setCccd(dtoCreateUserEmployee.getCccd());
        dtbEmployees.setDescription(dtoCreateUserEmployee.getDescription());
        dtbEmployees.setStartDate(dtoCreateUserEmployee.getStartDate());
        dtbEmployees.setEndDate(dtoCreateUserEmployee.getEndDate());
        dtbEmployees.setStatus(dtoCreateUserEmployee.getStatus());
        dtbEmployees.setPosition(dtbPosition);
        dtbEmployees.setDepartment(dtbDepartment);
//        dtbEmployees.setDepartment(dtbDepartment);
        dtbEmployees.setEmployeesCode(dtoCreateUserEmployee.getEmployeesCode());
        dtbEmployees.setManages(dtoCreateUserEmployee.getManages());
        dtbEmployees.setAllowance(dtoCreateUserEmployee.getAllowance());
        dtbEmployees.setBaseSalary(dtoCreateUserEmployee.getBaseSalary());

        return dtbEmployees;
    }

    // Entity sang dto
    public static DtoEmployeeRep toDto(DtbEmployees dtbEmployees) {
        DtoEmployeeRep dto = new DtoEmployeeRep();

        dto.setEmployeeId(dtbEmployees.getId());
        dto.setFirstName(dtbEmployees.getFirstName());
        dto.setLastName(dtbEmployees.getLastName());
        dto.setFullName(dtbEmployees.getFullName());
//        dto.setUsername(dtbEmployees.getUsername());
        dto.setProvince(dtbEmployees.getProvince());
        dto.setDistrict(dtbEmployees.getDistrict());
        dto.setAddress(dtbEmployees.getAddress());
        dto.setGender(dtbEmployees.getGender());
        dto.setBirthday(dtbEmployees.getBirthday());
        dto.setEmail(dtbEmployees.getEmail());
        dto.setPhone(dtbEmployees.getPhone());
        dto.setCccd(dtbEmployees.getCccd());
        dto.setDescription(dtbEmployees.getDescription());
        dto.setStartDate(dtbEmployees.getStartDate());
        dto.setEndDate(dtbEmployees.getEndDate());
        dto.setStatus(dtbEmployees.getStatus());
        dto.setPositionName(
                dtbEmployees.getPosition() != null
                ? dtbEmployees.getPosition().getPositionName()
                : null
        );
        dto.setDepartmentName(
                dtbEmployees.getDepartment() != null
                ? dtbEmployees.getDepartment().getDepartmentName()
                : null
        );
//        dto.setDepartment(dtbEmployees.getDepartment());
        dto.setEmployeesCode(dtbEmployees.getEmployeesCode());
        dto.setManages(dtbEmployees.getManages());
        dto.setAllowance(dtbEmployees.getAllowance());
        dto.setBaseSalary(dtbEmployees.getBaseSalary());

        return dto;
    }
}

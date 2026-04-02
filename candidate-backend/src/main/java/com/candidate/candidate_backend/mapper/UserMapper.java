package com.candidate.candidate_backend.mapper;

import com.candidate.candidate_backend.dto.employee.DtoEmployeeRep;
import com.candidate.candidate_backend.dto.user.DtoCreateUserEmployee;
import com.candidate.candidate_backend.dto.user.DtoUserRep;
import com.candidate.candidate_backend.dto.user.DtoUserReq;
import com.candidate.candidate_backend.entity.DtbRole;
import com.candidate.candidate_backend.entity.DtbUser;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {
    // dto sang entity
    public static DtbUser toEntity(DtoUserReq dto) {
        DtbUser user = new DtbUser();

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword()); // Encode ở Service
//        user.setFirstName(dto.getFirstName());
//        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        return user;
    }

    public static DtbUser toEntityUser(DtoCreateUserEmployee dtoCreateUserEmployee) {
        DtbUser user = new DtbUser();

        user.setUsername(dtoCreateUserEmployee.getUsername());
        user.setEmail(dtoCreateUserEmployee.getEmail());

        return user;
    }

    // Entity sang dto
    public static DtoUserRep toDto(DtbUser user) {
        DtoUserRep dto = new DtoUserRep();

        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
//        dto.setPassword(user.getPassword());
//        dto.setFirstName(user.getFirstName());
//        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());

        Set<String> roleNames = user.getRoles()
                .stream()
                .map(DtbRole::getRoleName) // ví dụ ADMIN
                .collect(Collectors.toSet());

        dto.setRoles(roleNames);

        if (user.getEmployee() != null) {
            DtoEmployeeRep dtoEmployeeRep = EmployeeMapper.toDto(user.getEmployee());
            dto.setEmployee(dtoEmployeeRep);
        } else {
            dto.setEmployee(null); // hoặc giá trị mặc định bạn muốn
        }

        return dto;
    }

}

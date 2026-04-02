package com.candidate.candidate_backend.mapper;

import com.candidate.candidate_backend.dto.Role.DtoRoleRep;
import com.candidate.candidate_backend.dto.Role.DtoRoleReq;
import com.candidate.candidate_backend.entity.DtbPermission;
import com.candidate.candidate_backend.entity.DtbRole;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleMapper {
    public static DtbRole toEntity(DtoRoleReq dto) {
        DtbRole role = new DtbRole();

        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());

        return role;
    }

    // Entity sang dto
    public static DtoRoleRep toDto(DtbRole role) {
        DtoRoleRep dto = new DtoRoleRep();

        dto.setId(role.getId());
        dto.setRoleName(role.getRoleName());
        dto.setDescription(role.getDescription());

        Set<String> permissionNames = role.getPermissionName()
                .stream()
                .map(DtbPermission::getPermissionName) // ví dụ ADMIN
                .collect(Collectors.toSet());

        dto.setPermission(permissionNames);

        return dto;
    }
}

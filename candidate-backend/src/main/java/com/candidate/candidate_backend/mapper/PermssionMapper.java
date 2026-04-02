package com.candidate.candidate_backend.mapper;

import com.candidate.candidate_backend.dto.perission.DtoPermissionRep;
import com.candidate.candidate_backend.dto.perission.DtoPermissionReq;
import com.candidate.candidate_backend.entity.DtbPermission;

public class PermssionMapper {
    public static DtbPermission toEntity(DtoPermissionReq dto) {
        DtbPermission permission = new DtbPermission();

        permission.setPermissionName(dto.getPermissionName());
        permission.setDescription(dto.getDescription());

        return permission;
    }

    // Entity sang dto
    public static DtoPermissionRep toDto(DtbPermission role) {
        DtoPermissionRep dto = new DtoPermissionRep();

        dto.setId(role.getId());
        dto.setPermissionName(role.getPermissionName());
        dto.setDescription(role.getDescription());

        return dto;
    }
}

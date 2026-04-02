package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.perission.DtoPermissionRep;
import com.candidate.candidate_backend.dto.perission.DtoPermissionReq;
import com.candidate.candidate_backend.entity.DtbPermission;
import com.candidate.candidate_backend.mapper.PermssionMapper;
import com.candidate.candidate_backend.repositorry.PermissionRepository;
import com.candidate.candidate_backend.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public CommonsRep createPermission(DtoPermissionReq dtoPermissionReq) {
        if (permissionRepository.existsByPermissionName(dtoPermissionReq.getPermissionName())) {
            return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "permission đã tồn tại", null);
        }

        // Map từ DTO sang Entity
        DtbPermission user = PermssionMapper.toEntity(dtoPermissionReq);

        // save vào DB
        DtbPermission saved = permissionRepository.save(user);

        // map Entity sang Dto
        DtoPermissionRep response = PermssionMapper.toDto(saved);

        return Helper.getServerResponse(HttpStatus.OK, "Thêm Permission Thành công", response);
    }

    public CommonsRep getPermission() {
        List<DtbPermission> permissionRepPage = permissionRepository.findAll();

        return Helper.getServerResponse(HttpStatus.OK, "Lây ra danh sách permission thành công", permissionRepPage);
    }

    public CommonsRep getPermissionById(String permisstionId) {
        UUID uuidPermisstionId = UUID.fromString(permisstionId);

        DtbPermission dtbPermission = permissionRepository.findById(uuidPermisstionId).orElseThrow(() -> new RuntimeException("Role not found"));

        DtoPermissionRep response = PermssionMapper.toDto(dtbPermission);

        return Helper.getServerResponse(HttpStatus.OK, "Lấy Chi tiết permission thành công", response);
    }

    public CommonsRep deletePermission(String permisstionId) {
        try {
            UUID uuidPermisstionId = UUID.fromString(permisstionId);
            permissionRepository.deleteById(uuidPermisstionId);
            return Helper.getServerResponse(HttpStatus.OK, "Xóa permission thành công", null);
        } catch (IllegalArgumentException e) {
            return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "Invalid UUID format", null);
        }
    }

    public CommonsRep updatePermission(String permisstionId,DtoPermissionReq dtoPermissionReq) {
        UUID uuidPermisstionId = UUID.fromString(permisstionId);

        DtbPermission permission =
                permissionRepository.findById(uuidPermisstionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Is not exist"));

        if (!hasChangeRole(permission, dtoPermissionReq)) {
            return Helper.getServerResponse(HttpStatus.OK, "Không có thay đổi Role", null);
        }

        permission.setPermissionName(dtoPermissionReq.getPermissionName());
        permission.setDescription(dtoPermissionReq.getDescription());

        DtbPermission dtbRole = permissionRepository.save(permission);

        DtoPermissionRep response = PermssionMapper.toDto(dtbRole);

        return Helper.getServerResponse(HttpStatus.OK, "Cập nhật thành công", response);
    }

    public boolean hasChangeRole (DtbPermission dtbPermission, DtoPermissionReq dtoPermissionReq) {
        return !Objects.equals(dtbPermission.getPermissionName(), dtoPermissionReq.getPermissionName()) ||
                !Objects.equals(dtbPermission.getDescription(), dtoPermissionReq.getDescription());
    }
}

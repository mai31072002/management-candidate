package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.Role.DtoRoleRep;
import com.candidate.candidate_backend.dto.Role.DtoRoleReq;
import com.candidate.candidate_backend.entity.DtbPermission;
import com.candidate.candidate_backend.entity.DtbRole;
import com.candidate.candidate_backend.mapper.RoleMapper;
import com.candidate.candidate_backend.repositorry.PermissionRepository;
import com.candidate.candidate_backend.repositorry.RoleRepository;
import com.candidate.candidate_backend.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    public CommonsRep createRole(DtoRoleReq dtoRoleReq) {

        if (roleRepository.existsByRoleName(dtoRoleReq.getRoleName())) {
            return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "role đã tồn tại", null);
        }

        // Map từ DTO sang Entity
        DtbRole role = RoleMapper.toEntity(dtoRoleReq);

        Set<DtbPermission> permissionsEntities = dtoRoleReq.getPermission()
                .stream()
                .map(permissionName -> permissionRepository.findByPermissionName(permissionName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + permissionName)))
                .collect(Collectors.toSet());

        role.setPermissionName(permissionsEntities);

        // save vào DB
        DtbRole saved = roleRepository.save(role);

        // map Entity sang Dto
        DtoRoleRep response = RoleMapper.toDto(saved);

        return Helper.getServerResponse(HttpStatus.OK, "Thêm User Thành công", response);
    }

    public CommonsRep getRole() {
        List<DtbRole> dtbRoles = roleRepository.findAll();

        return Helper.getServerResponse(HttpStatus.OK, "Lây ra danh sách role thành công", dtbRoles);
    }

    public CommonsRep getRoleById(String roleId) {
        UUID uuidRoleId = UUID.fromString(roleId);

        DtbRole dtbRole = roleRepository.findById(uuidRoleId).orElseThrow(() -> new RuntimeException("Role not found"));

        DtoRoleRep response = RoleMapper.toDto(dtbRole);

        return Helper.getServerResponse(HttpStatus.OK, "Lấy Chi tiết Role thành công", response);
    }

    public CommonsRep deleteRole(String roleId) {
        try {
            UUID uuidRoleId = UUID.fromString(roleId);
            roleRepository.deleteById(uuidRoleId);
            return Helper.getServerResponse(HttpStatus.OK, "Xóa role thành công", null);
        } catch (IllegalArgumentException e) {
            return Helper.getServerResponse(HttpStatus.BAD_REQUEST, "Invalid UUID format", null);
        } catch (Exception e) {
            return Helper.getServerResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot delete role : " + e.getMessage(), null);
        }
    }

    public CommonsRep updateRole(String roleId,DtoRoleReq dtoRoleReq) {
        UUID uuidRoleId = UUID.fromString(roleId);

        DtbRole role =
                roleRepository.findById(uuidRoleId).orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Is not exist"));

        if (!hasChangeRole(role, dtoRoleReq) && !hasPermissionChanged(role, dtoRoleReq)) {
            return Helper.getServerResponse(HttpStatus.OK, "Không có thay đổi Role", null);
        }

        role.setRoleName(dtoRoleReq.getRoleName());
        role.setDescription(dtoRoleReq.getDescription());

        if (hasPermissionChanged(role, dtoRoleReq)) {
            Set<DtbPermission> permissionsEntities = dtoRoleReq.getPermission()
                    .stream()
                    .map(permissionName -> permissionRepository.findByPermissionName(permissionName)
                            .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionName)))
                    .collect(Collectors.toSet());

            role.setPermissionName(permissionsEntities);
        }

        DtbRole dtbRole = roleRepository.save(role);

        DtoRoleRep response = RoleMapper.toDto(dtbRole);

        return Helper.getServerResponse(HttpStatus.OK, "Cập nhật thành công", response);
    }

    public boolean hasChangeRole (DtbRole dtbRole, DtoRoleReq dtoRoleReq) {
        return !Objects.equals(dtbRole.getRoleName(), dtoRoleReq.getRoleName()) ||
                !Objects.equals(dtbRole.getDescription(), dtoRoleReq.getDescription());
    }
    private boolean hasPermissionChanged(DtbRole dtbRole, DtoRoleReq dtoRoleReq) {
        Set<String> currentRoles = dtbRole.getPermissionName()
                .stream()
                .map(DtbPermission::getPermissionName)
                .collect(Collectors.toSet());

        Set<String> requestRoles = new HashSet<>(dtoRoleReq.getPermission());

        return !currentRoles.equals(requestRoles);
    }
}

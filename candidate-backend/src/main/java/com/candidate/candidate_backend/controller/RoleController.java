package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.Role.DtoRoleReq;
import com.candidate.candidate_backend.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("@ss.hasPermission('ROLE_CREATE')")
    @PostMapping
    public CommonsRep createRole(@RequestBody @Valid DtoRoleReq roleReq) {
        return roleService.createRole(roleReq);
    }

    @PreAuthorize("@ss.hasPermission('ROLE_VIEW')")
    @GetMapping
    public CommonsRep getRole() {
        return roleService.getRole();
    }

    @PreAuthorize("@ss.hasPermission('ROLE_VIEW_DETAIL')")
    @GetMapping("/{roleId}")
    public CommonsRep getRoleById(@PathVariable String roleId) {
        return roleService.getRoleById(roleId);
    }

    @PreAuthorize("@ss.hasPermission('ROLE_UPDATE')")
    @PutMapping("/{roleId}")
    public CommonsRep updateRole(
            @Valid
            @PathVariable String roleId,
            @RequestBody DtoRoleReq dtoRoleReq
    ) {
        return roleService.updateRole(roleId, dtoRoleReq);
    }

    @PreAuthorize("@ss.hasPermission('ROLE_DELETE')")
    @DeleteMapping("/{roleId}")
    public CommonsRep deleteRole(@PathVariable String roleId) {
        return roleService.deleteRole(roleId);
    }
}

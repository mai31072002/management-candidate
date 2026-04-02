package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.perission.DtoPermissionReq;
import com.candidate.candidate_backend.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PreAuthorize("@ss.hasPermission('PERMISSION_CREATE')")
    @PostMapping
    public CommonsRep createPermission(@RequestBody @Valid DtoPermissionReq dtoPermissionReq) {
        return permissionService.createPermission(dtoPermissionReq);
    }

    @PreAuthorize("@ss.hasPermission('PERMISSION_VIEW')")
    @GetMapping
    public CommonsRep getPermission() {
        return permissionService.getPermission();
    }

    @PreAuthorize("@ss.hasPermission('PERMISSION_VIEW_DETAIL')")
    @GetMapping("/{permissionId}")
    public CommonsRep getPermissionById(@PathVariable String permissionId) {
        return permissionService.getPermissionById(permissionId);
    }

    @PreAuthorize("@ss.hasPermission('PERMISSION_UPDATE')")
    @PutMapping("/{permissionId}")
    public CommonsRep updatePermission(
            @Valid
            @PathVariable String permissionId,
            @RequestBody DtoPermissionReq dtoPermissionReq
    ) {
        return permissionService.updatePermission(permissionId, dtoPermissionReq);
    }

    @PreAuthorize("@ss.hasPermission('PERMISSION_DELETE')")
    @DeleteMapping("/{permissionId}")
    public CommonsRep deletePermission(@PathVariable String permissionId) {
        return permissionService.deletePermission(permissionId);
    }
}

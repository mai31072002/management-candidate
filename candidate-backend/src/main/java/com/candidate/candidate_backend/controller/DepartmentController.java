package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.department.DtoDepartmentReq;
import com.candidate.candidate_backend.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PreAuthorize("@ss.hasPermission('DEPARTMENT_CREATE')")
    @PostMapping
    public CommonsRep createDepartment(@RequestBody @Valid DtoDepartmentReq dtoDepartmentReq) {
        return departmentService.createDepartment(dtoDepartmentReq);
    }

    @PreAuthorize("@ss.hasPermission('DEPARTMENT_VIEW')")
    @GetMapping
    public CommonsRep getDepartment() {
        return departmentService.getDepartment();
    }

    @PreAuthorize("@ss.hasPermission('DEPARTMENT_VIEW_DETAIL')")
    @GetMapping("/{departmentId}")
    public CommonsRep getDepartmentById(@PathVariable(name = "departmentId") String departmentId) {
        return departmentService.getDepartmentById(departmentId);
    }

    @PreAuthorize("@ss.hasPermission('DEPARTMENT_DELETE')")
    @DeleteMapping("/{departmentId}")
    public CommonsRep deleteDepartment(@PathVariable String departmentId) {
        return departmentService.deleteDepartment(departmentId);
    }

    @PreAuthorize("@ss.hasPermission('DEPARTMENT_UPDATE')")
    @PutMapping("/{departmentId}")
    public CommonsRep updateDepartment(
            @Valid
            @PathVariable(name = "departmentId") String departmentId,
            @RequestBody DtoDepartmentReq dtoDepartmentReq
            ) {
        return departmentService.updateDepartment(departmentId, dtoDepartmentReq);
    }
}

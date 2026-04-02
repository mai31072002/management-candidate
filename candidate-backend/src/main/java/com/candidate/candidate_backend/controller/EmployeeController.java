package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.employee.DtoEmployeeReq;
import com.candidate.candidate_backend.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PreAuthorize("@ss.hasPermission('EMPLOYEE_CREATE')")
    @PostMapping
    public CommonsRep createEmployee(@RequestBody @Valid DtoEmployeeReq dtoEmployeeReq) {
        return employeeService.createEmployee(dtoEmployeeReq);
    }

    @PreAuthorize("@ss.hasPermission('EMPLOYEE_VIEW')")
    @GetMapping
    public CommonsRep getByEmployee(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return employeeService.getByEmployee(page, size);
    }

    @PreAuthorize("@ss.hasPermission('EMPLOYEE_VIEW_DETAIL')")
    @GetMapping("/{employeeId}")
    public CommonsRep getEmployeeById(@PathVariable(name = "employeeId") String employeeId) {
        return employeeService.getEmployeeById(employeeId);
    }

    @PreAuthorize("@ss.hasPermission('EMPLOYEE_DELETE')")
    @DeleteMapping("/{employeeId}")
    public CommonsRep deleteEmployee(@PathVariable String employeeId) {
        return employeeService.deleteEmployee(employeeId);
    }

    @PreAuthorize("@ss.hasPermission('EMPLOYEE_UPDATE')")
    @PutMapping("/{employeeId}")
    public CommonsRep updateDepartment(
            @Valid
            @PathVariable(name = "employeeId") String employeeId,
            @RequestBody DtoEmployeeReq dtoEmployeeReq
            ) {
        return employeeService.updateEmployee(employeeId, dtoEmployeeReq);
    }

    @PreAuthorize("@ss.hasPermission('EMPLOYEE_SEARCH')")
    @GetMapping("search-employee")
    public CommonsRep searchFullName(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return employeeService.searchFullName(keyword, page, size);
    }

}

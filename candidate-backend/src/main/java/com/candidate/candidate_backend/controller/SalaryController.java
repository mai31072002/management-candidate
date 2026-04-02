package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.service.SalariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/salary")
public class SalaryController {

    @Autowired
    private SalariesService salariesService;

    @PreAuthorize("@ss.hasPermission('SALARY_CREATE')")
    @PostMapping
    public CommonsRep CountSalary(
            @RequestParam(name = "employeeId") String employeeId,
            @RequestParam(name = "month") YearMonth month
            ) {
        return salariesService.CountSalary(employeeId, month);
    }

    @PreAuthorize("@ss.hasPermission('SALARY_VIEW')")
    @GetMapping
    public CommonsRep getSalary() {
        return salariesService.getSalary();
    }

    @PreAuthorize("@ss.hasPermission('SALARY_VIEW_DETAIL')")
    @GetMapping("/{employeeId}")
    public CommonsRep getSalaryByEmployeeId(
            @PathVariable(name = "employeeId") String employeeId,
            @RequestParam(name = "yearMonth") YearMonth yearMonth
    ) {
        return salariesService.getSalaryByEmployeeId(employeeId, yearMonth);
    }
}

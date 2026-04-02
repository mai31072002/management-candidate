package com.candidate.candidate_backend.dto.user;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoUserReq {
    // User fields
    @NotBlank
    @Size(min = 4, max = 128, message = "Username trong khoảng từ 4–100 ký tự")
    private String username;

    private String password;

    @Email
    private String email;

    private Set<String> roles;

    // Employee fields
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String province;
    private String district;
    private Integer gender;
    private LocalDate birthday;
    private LocalDate startDate;
    private LocalDate endDate;
    private String employeesCode;
    private BigDecimal baseSalary;
    private BigDecimal allowance;
    private String manages;
    private String description;
    private Integer status;
    private String cccd;
    
    // Foreign keys
    private UUID positionId;
    private UUID departmentId;
}

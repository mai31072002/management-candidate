package com.candidate.candidate_backend.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoEmployeeReq {
    private String firstName;
    private String lastName;
    private  String fullName;

    @NotBlank
    private String username;

    @NotBlank
    private String province;

    @NotBlank
    private String district;

    @NotBlank
    private String address;

    private Integer gender;

    @NotNull
    private LocalDate birthday;

    @Email
    private String email;

//    @NotNull
    @Size(min = 9, max = 15, message = "SDT phải từ 9 -> 15 k tự")
    private String phone;

    @Size(max = 24, message = "cccd < 25 ký tự")
    private String cccd;

    private String description;

    @NotNull
    private LocalDate startDate; // ngày vào

    private LocalDate endDate; // Ngày kết thúc

    @NotNull
    private Integer status;

    @NotNull
    private UUID positionId; // chức vụ

    @NotNull
    private UUID departmentId;

    private String employeesCode;

    private BigDecimal baseSalary;

    private BigDecimal allowance;

    @NotBlank
    private String manages; // người quản lý

    private Set<String> roles;
}

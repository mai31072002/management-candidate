package com.candidate.candidate_backend.dto.user;

import jakarta.validation.constraints.*;
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
public class DtoCreateUserEmployee {
    @NotBlank
    @Size(min = 4, max = 128, message = "Username trong khoảng từ 4–100 ký tự")
    private String username;

    @NotBlank
    @Size(min = 6, max = 128, message = "Password phải ≥ 6 ký tự")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "Password phải có chữ hoa, chữ thường và số"
    )
    private String password;

    @Email
    private String email;
    
    private String firstName;
    private String lastName;
    private  String fullName;

    @NotBlank
    private String province;

    @NotBlank
    private String district;

    @NotBlank
    private String address;

    private Integer gender;

    @NotNull
    private LocalDate birthday;

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

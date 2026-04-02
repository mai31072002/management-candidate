package com.candidate.candidate_backend.dto.employee;

import com.candidate.candidate_backend.entity.DtbDepartment;
import com.candidate.candidate_backend.entity.DtbPosition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoEmployeeRep {
    private UUID employeeId;
    private String firstName;
    private String lastName;
    private  String fullName;
    private String username;
    private String province;
    private String district;
    private String address;
    private Integer gender;
    private LocalDate birthday;
    private String email;
    private String phone;
    private String cccd;
    private String description;
    private LocalDate startDate; // ngày vào
    private LocalDate endDate; // Ngày kết thúc
    private Integer status;
    private String positionName; // chức vụ
    private String departmentName;

    private String employeesCode;
    private BigDecimal baseSalary;
    private BigDecimal allowance;
    private String manages; // người quản lý
}

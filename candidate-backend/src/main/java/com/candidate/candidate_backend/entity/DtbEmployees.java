package com.candidate.candidate_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

// Loại bỏ field hibernateLazyInitializer khi trả về
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DtbEmployees extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private UUID Id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_name")
    private String fullName;
//    private String username;
    private String province;
    private String district;
    private String address;
    private Integer gender;
    private LocalDate birthday;
    private String email;
    private String phone;
    private String cccd;
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate; // ngày vào
    @Column(name = "end_date")
    private LocalDate endDate; // Ngày kết thúc
    private Integer status;

    // nullable = false : Cấm giá trị null, buộc phải có giá trị truyền vào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private DtbPosition position; // chức vụ

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private DtbDepartment department; // Phòng ban
//    private String department;

    @Column(name = "base_salary", precision = 15, scale = 2)
    private BigDecimal baseSalary; // Lương cơ bản

    private BigDecimal allowance; // Phụ cấp

    private String employeesCode;
    private String manages; // người quản lý

    // cascade = CascadeType.ALL : Khi employees thay đổi thì user cũng thay đổi theo
    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL)
    private DtbUser user;
}

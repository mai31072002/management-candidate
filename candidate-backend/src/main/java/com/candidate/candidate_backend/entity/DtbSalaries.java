package com.candidate.candidate_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dtb_salaries")
public class DtbSalaries extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employees_id", nullable = false)
//    private UUID employeesId;

    private UUID userId;

    @Column(nullable = false)
    private YearMonth month;

    @Column(name = "base_salary", precision = 15, scale = 2)
    private BigDecimal baseSalary; // Lương cơ bản

    @Column(name = "total_work_days")
    private int totalWorkDays;

    @Column(name = "ot_amount", precision = 15, scale = 2)
    private BigDecimal otAmount; // Tổng tiền lương tăng ca

    @Column(precision = 15, scale = 2)
    private BigDecimal allowance; // Phụ cập : phụ cấp + thưởng

    @Column(precision = 15, scale = 2)
    private BigDecimal deductions; // Các khoản giảm trừ

    @Column(name = "net_salary", precision = 15, scale = 2)
    private BigDecimal netSalary; // Lương thực thế : base + ot + allowance - deductions
}

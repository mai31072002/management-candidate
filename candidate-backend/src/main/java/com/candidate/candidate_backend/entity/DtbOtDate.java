package com.candidate.candidate_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dtb_ot_date")
public class DtbOtDate extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "time_keeping_id", nullable = false)
//    private DtbTimeKeeping timeKeeping;

//    @Column(name = "employee_id")
//    private UUID employeeId;
//
//    @Column(name = "user_id")
//    private UUID userId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employee_id", insertable = false, updatable = false)
//    private DtbEmployees employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private DtbUser user;

    private LocalDate workDate;

    private Integer OtType; // 0: Thường/ 1: đêm / 2: Lễ
    private Integer otMinutes; // Tổng phút Ot
    private LocalTime startTime;
    private LocalTime endTime;
    private String jobTitle;

    private String approvedBy; // Ai duyệt

    @Column(
        columnDefinition = "timestamp default current_timestamp",
        insertable = false,
        updatable = false
    )
    private LocalDateTime approvedAt; // Thời gian duyệt

    private BigDecimal otRate; // hệ số nhân
    private int status; // 0: chờ duyệt, 1: đã duyệt, 2: không được duyệt
}

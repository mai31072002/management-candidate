package com.candidate.candidate_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dtb_time_keeping")
public class DtbTimeKeeping extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "employees_id")
    private UUID employeeId;
    @Column(name = "user_id")
    private UUID userId;
    private LocalDate workDate;
    private LocalTime checkIn;
    private LocalTime checkOut;
//    private int total_work_minutes; // Tổng số giờ làm trong ngày
    private Integer lateMinutes; // đi muộn
    private Integer earlyLeaveMinutes; // về sớm
    private int status; // 0: nghỉ, 1.Hợp lệ, 2. đi muộn về sớm
}

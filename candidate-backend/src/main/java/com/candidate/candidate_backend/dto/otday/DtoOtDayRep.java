package com.candidate.candidate_backend.dto.otday;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoOtDayRep {
    private UUID id;

//    private UUID timeKeepingId;
//    private UUID employeeId;
    private UUID userId;
    private LocalDate workDate;
    private Integer OtType; // Thường/ đêm / Lễ
    private Integer otMinutes; // Tổng phút Ot
    private LocalTime startTime;
    private LocalTime endTime;
    private String jobTitle;
    private String approvedBy; // Ai duyệt
    private LocalDateTime approvedAt; // Thời gian duyệt

    private BigDecimal otRate;
    private int status; // 0: chờ duyệt, 1: đã duyệt, 2: không được duyệt

    private String fullName;
}

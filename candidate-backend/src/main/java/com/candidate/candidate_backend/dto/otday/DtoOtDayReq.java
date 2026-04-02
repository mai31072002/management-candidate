package com.candidate.candidate_backend.dto.otday;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoOtDayReq {
//    @NotNull
//    private UUID timeKeepingId;

//    @NotNull
//    private UUID employeeId;

    @NotNull
    private UUID userId;

    @NotNull
    private LocalDate workDate;

    @NotNull
    private Integer OtType; // Thường/ đêm / Lễ

    @NotNull
    private Integer otMinutes; // Tổng phút Ot

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    @NotBlank
    private String jobTitle;
    private String approvedBy; // Ai duyệt
    private LocalDateTime approvedAt; // Thời gian duyệt

    private BigDecimal otRate;
    private int status; // 0: chờ duyệt, 1: đã duyệt, 2: không được duyệt
}

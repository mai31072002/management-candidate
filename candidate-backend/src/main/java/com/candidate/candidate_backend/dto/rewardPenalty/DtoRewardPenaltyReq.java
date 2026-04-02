package com.candidate.candidate_backend.dto.rewardPenalty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
public class DtoRewardPenaltyReq {
//    @NotNull
//    private UUID employeeId;

    @NotNull
    private UUID userId;

    @NotNull
    private LocalDate month; // "2025-01"

    @NotNull
    private BigDecimal amount;

    @NotNull
    private int type; // BONUS / PENALTY (Thưởng/phạt)
    private String reason;
}

package com.candidate.candidate_backend.dto.rewardPenalty;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DtoRewardPenaltyRep {
    private UUID id;
//    private UUID employeeId;
    private UUID userId;
    private String employeeName;
    private LocalDate month;
    private BigDecimal amount;
    private int type;
    private String reason;
}

package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.rewardPenalty.DtoRewardPenaltyReq;
import com.candidate.candidate_backend.service.RewardPenaltyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reward-penalty")
public class RewardPenaltyController {

    @Autowired
    private RewardPenaltyService rewardPenaltyService;

    @PreAuthorize("@ss.hasPermission('REWARD_PENALTY_CREATE')")
    @PostMapping
    public CommonsRep createRewardPenalty(@Valid @RequestBody DtoRewardPenaltyReq dtoRewardPenaltyReq) {
        return rewardPenaltyService.createRewardPenalty(dtoRewardPenaltyReq);
    }

//    @GetMapping("/{employeeId}")
//    public CommonsRep getByEmployeeAndMonth(
//            @Valid
//            @PathVariable(name = "employeeId") String employeeId,
//            @RequestParam(name = "month") String month
//    ) {
//        return rewardPenaltyService.getByEmployeeAndMonth(employeeId, month);
//    }

    @PreAuthorize("@ss.hasPermission('REWARD_PENALTY_VIEW_DETAIL')")
    @GetMapping("/{userId}")
    public CommonsRep getRewardPenaltyByEmployee(
            @Valid
            @PathVariable(name = "userId") String userId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam(required = false) Integer type

    ) {
        return rewardPenaltyService.getRewardPenaltyByEmployee( userId, fromDate, toDate, type);
    }

    @PreAuthorize("@ss.hasPermission('REWARD_PENALTY_UPDATE')")
    @PutMapping("/{rewardPenaltyId}")
    public CommonsRep updateRewardPenalty(
            @Valid
            @PathVariable(name = "rewardPenaltyId") String rewardPenaltyId,

            @RequestBody DtoRewardPenaltyReq dtoRewardPenaltyReq
    ) {
        return rewardPenaltyService.updateRewardPenalty(rewardPenaltyId, dtoRewardPenaltyReq);
    }

    @PreAuthorize("@ss.hasPermission('REWARD_PENALTY_DELETE')")
    @DeleteMapping("/{rewardPenaltyId}")
    public CommonsRep deleteRewardPenalty(@PathVariable(name = "rewardPenaltyId") String rewardPenaltyId) {
        return rewardPenaltyService.deleteRewardPenalty(rewardPenaltyId);
    }
}

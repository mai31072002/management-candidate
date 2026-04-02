package com.candidate.candidate_backend.mapper;

import com.candidate.candidate_backend.dto.rewardPenalty.DtoRewardPenaltyRep;
import com.candidate.candidate_backend.dto.rewardPenalty.DtoRewardPenaltyReq;
import com.candidate.candidate_backend.entity.DtbRewardPenalty;
import com.candidate.candidate_backend.entity.DtbUser;

public class RewardPenaltyMapper {
    public static DtbRewardPenalty toEntity(
            DtoRewardPenaltyReq dto,
            DtbUser user
    ) {
        DtbRewardPenalty entity = new DtbRewardPenalty();

        entity.setUser(user);
        entity.setMonth(dto.getMonth());
        entity.setAmount(dto.getAmount());
        entity.setType(dto.getType());
        entity.setReason(dto.getReason());

        return entity;
    }

    // Entity -> DTO
    public static DtoRewardPenaltyRep toDto(DtbRewardPenalty entity) {
        DtoRewardPenaltyRep dto = new DtoRewardPenaltyRep();

        dto.setId(entity.getId());
        dto.setUserId(entity.getUser().getUserId());
        dto.setEmployeeName(entity.getUser().getUsername());
        dto.setMonth(entity.getMonth());
        dto.setAmount(entity.getAmount());
        dto.setType(entity.getType());
        dto.setReason(entity.getReason());

        return dto;
    }
}

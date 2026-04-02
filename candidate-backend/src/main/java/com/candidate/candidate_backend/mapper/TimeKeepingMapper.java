package com.candidate.candidate_backend.mapper;

import com.candidate.candidate_backend.dto.timeKeeping.DtoTimeKeepingRep;
import com.candidate.candidate_backend.dto.timeKeeping.DtoTimeKeepingReq;
import com.candidate.candidate_backend.entity.DtbTimeKeeping;

public class TimeKeepingMapper {
    public static DtbTimeKeeping toEntity(DtoTimeKeepingReq dto) {
        DtbTimeKeeping dtbTimeKeeping = new DtbTimeKeeping();

        dtbTimeKeeping.setEmployeeId(dto.getUserId());
        dtbTimeKeeping.setWorkDate(dto.getWorkDate());
        dtbTimeKeeping.setCheckIn(dto.getCheckIn());
        dtbTimeKeeping.setCheckOut(dto.getCheckOut());
        dtbTimeKeeping.setStatus(dto.getStatus());

        return dtbTimeKeeping;
    }

    // Entity sang dto
    public static DtoTimeKeepingRep toDto(DtbTimeKeeping dtbTimeKeeping) {
        DtoTimeKeepingRep dto = new DtoTimeKeepingRep();

        dto.setId(dtbTimeKeeping.getId());
        dto.setUserId(dtbTimeKeeping.getUserId());
        dto.setWorkDate(dtbTimeKeeping.getWorkDate());
        dto.setCheckIn(dtbTimeKeeping.getCheckIn());
        dto.setCheckOut(dtbTimeKeeping.getCheckOut());
        dto.setStatus(dtbTimeKeeping.getStatus());

        return dto;
    }
}

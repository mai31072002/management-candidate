package com.candidate.candidate_backend.mapper;

import com.candidate.candidate_backend.dto.lever.DtoLeverRep;
import com.candidate.candidate_backend.dto.lever.DtoLeverReq;
import com.candidate.candidate_backend.entity.DtbLever;

public class LeverMapper {
    public static DtbLever toEntity(DtoLeverReq dto) {
        DtbLever dtbLever = new DtbLever();

//        dtbLever.setId(dto.getId());
        dtbLever.setLeverNumber(dto.getLeverNumber());
        dtbLever.setDescription(dto.getDescription());

        return dtbLever;
    }

    // Entity sang dto
    public static DtoLeverRep toDto(DtbLever dtbPosition) {
        DtoLeverRep dto = new DtoLeverRep();

        dto.setId(dtbPosition.getId());
        dto.setLeverNumber(dtbPosition.getLeverNumber());
        dto.setDescription(dtbPosition.getDescription());

        return dto;
    }
}

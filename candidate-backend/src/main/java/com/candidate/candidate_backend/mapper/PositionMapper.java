package com.candidate.candidate_backend.mapper;


import com.candidate.candidate_backend.dto.position.DtoPositionRep;
import com.candidate.candidate_backend.dto.position.DtoPositionReq;
import com.candidate.candidate_backend.entity.DtbLever;
import com.candidate.candidate_backend.entity.DtbPosition;

public class PositionMapper {
    public static DtbPosition toEntity(DtoPositionReq dto, DtbLever dtbLever) {
        DtbPosition position = new DtbPosition();

//        department.setId(dto.getId());
        position.setPositionName(dto.getPositionName());
        position.setDescription(dto.getDescription());
        position.setLever(dtbLever);

        return position;
    }

    // Entity sang dto
    public static DtoPositionRep toDto(DtbPosition dtbPosition) {
        DtoPositionRep dto = new DtoPositionRep();

        dto.setId(dtbPosition.getId());
        dto.setPositionName(dtbPosition.getPositionName());
        dto.setDescription(dtbPosition.getDescription());
        dto.setLeverNumber(
                dtbPosition.getLever() != null
                ? dtbPosition.getLever().getLeverNumber()
                : null
        );

        return dto;
    }
}

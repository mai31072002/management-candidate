package com.candidate.candidate_backend.mapper;

import com.candidate.candidate_backend.dto.otday.DtoOtDayRep;
import com.candidate.candidate_backend.dto.otday.DtoOtDayReq;
import com.candidate.candidate_backend.entity.DtbOtDate;
import com.candidate.candidate_backend.entity.DtbUser;
import com.candidate.candidate_backend.repositorry.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class OtDayMapper {

    @Autowired
    private UserRepository userRepository;

    // DTO -> Entity
    public static DtbOtDate toEntity(DtoOtDayReq dto, DtbUser dtbUser) {
        DtbOtDate entity = new DtbOtDate();

//        entity.setTimeKeeping(dtbTimeKeeping);
//        entity.setEmployeeId(dto.getEmployeeId());
        entity.setUser(dtbUser);
        entity.setWorkDate(dto.getWorkDate());
        entity.setOtType(dto.getOtType());
        entity.setOtMinutes(dto.getOtMinutes());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setJobTitle(dto.getJobTitle());
        entity.setOtRate(dto.getOtRate());
        entity.setStatus(dto.getStatus()); // mặc định: chờ duyệt

        return entity;
    }

    // Entity -> DTO
    public static DtoOtDayRep toDto(DtbOtDate entity) {
        DtoOtDayRep dto = new DtoOtDayRep();

        dto.setId(entity.getId());
//        dto.setTimeKeepingId(entity.getTimeKeeping().getId());
//        dto.setEmployeeId(entity.getEmployeeId());
        UUID userId = entity.getUser() != null ? entity.getUser().getUserId() : null;
        dto.setUserId(userId);

        String fullName = entity.getUser() != null &&
                  entity.getUser().getEmployee() != null
                ? entity.getUser().getEmployee().getFullName()
                : null;

        dto.setFullName(fullName);
        dto.setWorkDate(entity.getWorkDate());
        dto.setOtType(entity.getOtType());
        dto.setOtMinutes(entity.getOtMinutes());
        dto.setStartTime(entity.getStartTime());
        dto.setEndTime(entity.getEndTime());
        dto.setJobTitle(entity.getJobTitle());
        dto.setApprovedBy(entity.getApprovedBy());
        dto.setApprovedAt(entity.getApprovedAt());
        dto.setOtRate(entity.getOtRate());
        dto.setStatus(entity.getStatus());

        return dto;
    }
}

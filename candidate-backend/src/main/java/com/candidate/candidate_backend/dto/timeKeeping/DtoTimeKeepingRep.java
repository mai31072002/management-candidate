package com.candidate.candidate_backend.dto.timeKeeping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoTimeKeepingRep {
    private UUID id;
//    private UUID employeesId;
    private UUID userId;
    private LocalDate workDate;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private int status;
}

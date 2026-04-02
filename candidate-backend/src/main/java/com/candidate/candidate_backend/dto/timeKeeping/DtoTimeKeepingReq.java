package com.candidate.candidate_backend.dto.timeKeeping;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class DtoTimeKeepingReq {
//    @NotNull
//    private UUID employeesId;

    @NotNull
    private UUID userId;

    @NotNull
    private LocalDate workDate;

    @NotNull
    private LocalTime checkIn;

    @NotNull
    private LocalTime checkOut;

    @NotNull
    private int status;
}

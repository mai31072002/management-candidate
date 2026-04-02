package com.candidate.candidate_backend.dto.lever;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoLeverRep {
    private UUID id;
    private Integer leverNumber;
    private String description;
}

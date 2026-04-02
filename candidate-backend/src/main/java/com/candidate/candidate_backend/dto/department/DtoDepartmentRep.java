package com.candidate.candidate_backend.dto.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoDepartmentRep {
    private UUID id;
    private String departmentName;
    private String description;
}

package com.candidate.candidate_backend.dto.position;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoPositionReq {
    @NotBlank
    @Size(max = 100)
    private String positionName;
    private String description;
    @NotBlank
    private String leverId;
//    private String department;
}

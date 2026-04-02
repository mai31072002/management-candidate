package com.candidate.candidate_backend.dto.perission;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoPermissionReq {
    @NotBlank
    private String permissionName;
    private String description;
}

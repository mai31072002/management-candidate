package com.candidate.candidate_backend.dto.perission;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoPermissionRep {
    private UUID id;
    private String permissionName;
    private String description;
}

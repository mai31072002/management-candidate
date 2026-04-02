package com.candidate.candidate_backend.dto.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoRoleRep {
    private UUID id;
    private String roleName;
    private String description;
    private Set<String> permission;
}

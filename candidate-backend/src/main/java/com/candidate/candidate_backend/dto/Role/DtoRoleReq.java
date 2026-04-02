package com.candidate.candidate_backend.dto.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoRoleReq {
    @NotBlank
    @Size(min = 1, max = 100, message = "roleName phải từ 1 -> 100 k tự")
    private String roleName;
    private String description;
    @NotEmpty(message = "Permission không được để trống")
    private Set<String> permission;
}

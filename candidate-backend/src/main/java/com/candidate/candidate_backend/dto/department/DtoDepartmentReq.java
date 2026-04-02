package com.candidate.candidate_backend.dto.department;

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
public class DtoDepartmentReq {
    @NotBlank
    @Size(min = 1, max = 254, message = "departmentName phải từ 1 -> 254 ký tự")
    private String departmentName;
    private String description;
}

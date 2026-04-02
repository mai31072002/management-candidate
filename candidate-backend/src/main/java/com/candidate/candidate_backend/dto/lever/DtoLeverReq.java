package com.candidate.candidate_backend.dto.lever;

import com.candidate.candidate_backend.validator.HbRequired;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoLeverReq {

//    @NotNull(message = "không được để trống")
//    @Min(value = 1, message = "Lever number phải lớn hơn 0")
    @HbRequired(name = "leverNumber")
    private Integer leverNumber;

//    @NotBlank(message = "không được để trống")
//    @Size(max = 255, message = "không được vượt quá 255 ký tự")
    @HbRequired(name = "description")
    private String description;
}

package com.candidate.candidate_backend.dto.login;

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
public class DtoLoginReq {
//    @NotBlank(message = "Không được để trống")
//    @Size(max = 128, message = "Phải trong khoảng 128 ký tự")
    @HbRequired(name = "username")
    private String username;

    @Email
    private String email;

//    @NotBlank(message = "Không được để trống")
//    @Size(max = 128, message = "Phải trong khoảng 128 ký tự")
    @HbRequired(name = "password")
    private String password;
}

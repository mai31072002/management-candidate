package com.candidate.candidate_backend.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoForgotPassword {
    @NotBlank(message = "Không được để trống")
    private String username;

    @NotBlank(message = "Không được để trống")
    @Size(min = 6, max = 128, message = "Phải nằm trong khoảng 6 - 128 k tự")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
        message = "Password phải có chữ hoa, chữ thường và số"
    )
    private String newPassword;

    @Email
    private String email;
}

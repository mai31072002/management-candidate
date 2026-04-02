package com.candidate.candidate_backend.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoRefreshToken {
    @NotBlank
    private String refreshToken;
}

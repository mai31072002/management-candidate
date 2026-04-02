package com.candidate.candidate_backend.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoLoginRep {
    private String username;
    private String accessToken;
    private String refreshToken;
}

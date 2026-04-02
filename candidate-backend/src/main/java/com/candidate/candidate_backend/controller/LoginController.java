package com.candidate.candidate_backend.controller;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.login.DtoForgotPassword;
import com.candidate.candidate_backend.dto.login.DtoLoginReq;
import com.candidate.candidate_backend.dto.login.DtoRefreshToken;
import com.candidate.candidate_backend.service.LoginService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public CommonsRep login(@RequestBody @Valid DtoLoginReq dtoLoginReq) {
        return loginService.login(dtoLoginReq);
    }

    @PostMapping("/refresh-token")
    public CommonsRep refreshToken(@RequestBody DtoRefreshToken dtoRefreshToken) {
        return loginService.refreshToken(dtoRefreshToken);
    }

    @PostMapping("/forgot-password")
    public CommonsRep forgotPassword(@RequestBody @Valid DtoForgotPassword dtoForgotPassword) {
        return loginService.forgotPassword(dtoForgotPassword);
    }
}

package com.candidate.candidate_backend.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        ResponseBody body = ResponseBody.of(request, authException, status);
        body.setType("AUTH");

        String authHeader = request.getHeader("Authorization");
        String title = resolveTitle(authHeader, authException);
        body.setTitle(title);
        body.addMsg(MsgInfo.by(title));

        response.setStatus(status.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), body);
    }

    private static String resolveTitle(String authHeader, Exception ex) {
        if (authHeader == null || authHeader.isBlank()) {
            return "MISSING_TOKEN";
        }

        Throwable root = ex;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        String msg = root.getMessage();
        if (msg == null) {
            msg = "";
        }
        String normalized = msg.toLowerCase();
        if (normalized.contains("expired")) {
            return "TOKEN_EXPIRED";
        }
        return "INVALID_TOKEN";
    }
}

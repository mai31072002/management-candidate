package com.candidate.candidate_backend.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("ss")
public class SecurityService {
    public boolean hasPermission(String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Nếu là ADMIN, cho qua luôn không cần check permission
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) return true;

        // Nếu không phải ADMIN, check xem có permission cụ thể không
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(permission));
    }
}

package com.candidate.candidate_backend.util;

import com.candidate.candidate_backend.dto.CommonsRep;
import com.candidate.candidate_backend.dto.PageableRep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Helper {

    public static CommonsRep getServerResponse(
            final HttpStatus httpStatus, final String message, final Object data) {
        CommonsRep response = new CommonsRep();
        response.setStatus(httpStatus.value());
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    public static <T> PageableRep mapToPageableRep(Page<T> page) {
        return new PageableRep(
                page.getNumber(),
                page.getSize(),
                page.getSort().toString(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public static boolean isAdmin() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    public static String convertMsisdn(String msisdn) {
        return msisdn.replaceFirst("^0", "84");
    }

}

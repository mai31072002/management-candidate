package com.candidate.candidate_backend.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BusinessException extends RuntimeException {
     private final HttpStatus status;

    public BusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}

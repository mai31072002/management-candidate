package com.candidate.candidate_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserNotFoundException extends RuntimeException {

    /**
    * @author chienoq
    * @date
    * @param message
    * @return
    */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
    * @author chienoq
    * @date
    * @param message
    * @param cause
    * @return
    */
    public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
    }
}

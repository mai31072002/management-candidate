package com.candidate.candidate_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UPGRADE_REQUIRED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResourceLimitException extends Exception {
      private Integer status;
      private String message;
}

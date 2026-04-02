package com.candidate.candidate_backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // không trả field về nếu null
public class CommonsRep {
    private int status;
    private String message;
    private Object data;
}

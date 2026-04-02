package com.candidate.candidate_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableRep {
    private int page;
    private int limit;
    private String sortBy;
    private long totalElements;
    private int totalPage;
}

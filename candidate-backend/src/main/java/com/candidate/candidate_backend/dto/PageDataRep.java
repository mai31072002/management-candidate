package com.candidate.candidate_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDataRep<T> {
    private List<T> data;
    private PageableRep pageableRep;
}

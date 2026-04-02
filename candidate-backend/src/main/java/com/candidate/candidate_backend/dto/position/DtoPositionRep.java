
package com.candidate.candidate_backend.dto.position;

import com.candidate.candidate_backend.entity.DtbDepartment;
import com.candidate.candidate_backend.entity.DtbLever;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoPositionRep {
    private UUID id;
    private String positionName;
    private String description;
    private Integer leverNumber;
}

package com.candidate.candidate_backend.dto.user;

import com.candidate.candidate_backend.dto.employee.DtoEmployeeRep;
import com.candidate.candidate_backend.entity.DtbEmployees;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoUserRep {
    private UUID userId;
//    private String firstName;
//    private String lastName;
    private String username;
//    private String password;
    private String email;
    private Set<String> roles;
    private DtoEmployeeRep employee;
}

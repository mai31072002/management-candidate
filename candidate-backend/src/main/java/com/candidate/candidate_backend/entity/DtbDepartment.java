package com.candidate.candidate_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dtb_department")
public class DtbDepartment extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String departmentName;
    private String description;
}

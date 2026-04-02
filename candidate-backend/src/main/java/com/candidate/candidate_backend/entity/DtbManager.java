package com.candidate.candidate_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtbManager extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "manager_id")
    private UUID Id;

    private String managerName;
}

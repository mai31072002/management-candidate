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
public class DtbPosition extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "position_name")
    private String positionName;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lever_id")
    private DtbLever lever;
}

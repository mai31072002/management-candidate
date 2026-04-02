package com.candidate.candidate_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dtb_role")
public class DtbRole extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String roleName;
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "dtb_role_permission",
        joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id")
    )
    private Set<DtbPermission> permissionName;
}
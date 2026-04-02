package com.candidate.candidate_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dtb_user")
public class DtbUser extends EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;
//    private String firstName;
//    private String lastName;
    private String username;
    private String password;
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "dtb_user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<DtbRole> roles;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private DtbEmployees employee;

    @Override
    public String toString() {
        return "DtbUser{" +
                "userId=" + userId +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
//                ", email='" + email + '\'' +
                '}';
    }
}

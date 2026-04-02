package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<DtbRole, UUID> {
    Optional<DtbRole> findByRoleName(String roleName);
    boolean existsByRoleName(String roleName);
//    Page<DtbRole> findByRoleNameContainingIgnoreCase(String keyword, Pageable pageable);
}

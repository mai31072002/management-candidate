package com.candidate.candidate_backend.repositorry;


import com.candidate.candidate_backend.entity.DtbPermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<DtbPermission, UUID> {
    Optional<DtbPermission> findByPermissionName(String permissionName);
    boolean existsByPermissionName(String permissionName);
//    Page<DtbPermission> findByPermissionContainingIgnoreCase(String keyword, Pageable pageable);
}

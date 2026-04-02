package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<DtbDepartment, UUID> {
    boolean existsByDepartmentName(String departmentName);
    List<DtbDepartment> findAllByIsDeletedFalse();

    Optional<DtbDepartment> findByIdAndIsDeletedFalse(UUID id);
    
    Optional<DtbDepartment> findByDepartmentNameAndIsDeletedFalse(String departmentName);
}

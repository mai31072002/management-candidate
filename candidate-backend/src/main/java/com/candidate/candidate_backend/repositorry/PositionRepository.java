package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<DtbPosition, UUID> {
    boolean existsByPositionName(String positionName);

    List<DtbPosition> findAllByIsDeletedFalse();

    Optional<DtbPosition> findByIdAndIsDeletedFalse(UUID id);
    
    Optional<DtbPosition> findByPositionNameAndIsDeletedFalse(String positionName);

    long countByLeverIdAndIsDeletedFalse(UUID leverId);
}

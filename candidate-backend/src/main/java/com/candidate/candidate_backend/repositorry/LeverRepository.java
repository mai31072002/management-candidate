package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbLever;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeverRepository extends JpaRepository<DtbLever, UUID> {
    boolean existsByLeverNumber(Integer leverNumber);

    List<DtbLever> findAllByIsDeletedFalse();

    Optional<DtbLever> findByIdAndIsDeletedFalse(UUID id);
}

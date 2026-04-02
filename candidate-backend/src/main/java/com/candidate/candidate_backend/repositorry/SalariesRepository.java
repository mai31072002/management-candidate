package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbEmployees;
import com.candidate.candidate_backend.entity.DtbSalaries;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.Optional;
import java.util.UUID;

public interface SalariesRepository extends JpaRepository<DtbSalaries, UUID> {
    Optional<DtbSalaries> findByUserIdAndMonth(
        UUID userId,
        YearMonth month
    );
}

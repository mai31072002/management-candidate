package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbTimeKeeping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TimeKeepingRepository extends JpaRepository<DtbTimeKeeping, UUID> {
    Page<DtbTimeKeeping> findAllByIsDeletedFalse(Pageable pageable);

    Optional<DtbTimeKeeping> findByIdAndIsDeletedFalse(UUID employeeId);

    @Query("""
            select count(t)
            from DtbTimeKeeping t
            where t.userId = :userId
            and month(t.workDate) = :month
            and year(t.workDate) = :year
            and t.status = 1
            """)
    Integer countWorkDays(UUID userId, int month, int year);

    @Query("""
        select t
        from DtbTimeKeeping t
        where t.userId = :userId
          and month(t.workDate) = :month
          and year(t.workDate) = :year
          and t.isDeleted = false
        order by t.workDate
    """)
    List<DtbTimeKeeping> findWorkDaysByUserAndMonth(
            UUID userId,
            int month,
            int year
    );

}

package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbOtDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface OtDayRepository extends JpaRepository<DtbOtDate, UUID> {
//    boolean existsByTimeKeepingIdAndIsDeletedFalse(UUID timekeepingId);

    Page<DtbOtDate> findAllByIsDeletedFalse(Pageable pageable);

//    Page<DtbOtDate> findAllByIsDeletedFalseAndWorkDateBetween(
//        LocalDate fromDate,
//        LocalDate toDate,
//        Pageable pageable
//    );
//
//    @Query("""
//        select o
//        from DtbOtDate o
//        join fetch o.employee
//        where o.workDate between :startDate and :endDate
//        order by o.workDate desc
//    """)
//    Page<DtbOtDate> findOtWithEmployee(
//        @Param("startDate") LocalDate startDate,
//        @Param("endDate") LocalDate endDate,
//        Pageable pageable
//    );

    @Query(""" 
            select o from DtbOtDate o 
            where o.workDate between :startDate and :endDate 
            AND (:status IS NULL OR o.status = :status) 
            order by o.workDate desc """)
    Page<DtbOtDate> findOtWithUser(
            @Param("status") Integer status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );

    @Query("""
            select coalesce(sum(o.otMinutes * o.otRate / 60))
            from DtbOtDate o
            where o.user.userId = :userId
              and month(o.workDate) = :month
              and year(o.workDate) = :year
              and o.status = :status
            """)
    BigDecimal sumOtHour(UUID userId, int month, int year, int status);

    @Query("""
        select o
        from DtbOtDate o
        where o.user.userId = :userId
          and o.workDate between :startDate and :endDate
          and o.isDeleted = false
        order by o.workDate asc
    """)
    List<DtbOtDate> findOtByUserAndMonth(
        @Param("userId") UUID userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("""
        select case when count(o) > 0 then true else false end
        from DtbOtDate o
        where o.user.userId = :userId
          and o.workDate = :workDate
          and o.isDeleted = false
          and o.startTime < :endTimeNew
          and o.endTime > :startTimeNew
    """)
    boolean existsOverlapOt(
        @Param("userId") UUID userId,
        @Param("workDate") LocalDate workDate,
        @Param("startTimeNew") LocalTime startTimeNew,
        @Param("endTimeNew") LocalTime endTimeNew
    );

}

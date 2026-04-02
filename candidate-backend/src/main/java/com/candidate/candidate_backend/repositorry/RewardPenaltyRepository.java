package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbRewardPenalty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RewardPenaltyRepository extends JpaRepository<DtbRewardPenalty, UUID> {
//    List<DtbRewardPenalty> findByEmployeeIdAndMonth(UUID employeeId, LocalDate month);

    boolean existsByUser_UserIdAndMonthAndType( UUID userId, LocalDate month, int type );

    Optional<DtbRewardPenalty> findByIdAndUser_UserId(
            UUID id, UUID userId
    );

    @Query("""
        select r
        from DtbRewardPenalty r
        join fetch r.user e
        where e.userId = :userId
          and r.month between :fromMonth and :toMonth
          and (:type is null or r.type = :type)
          and r.isDeleted = false
        order by r.month desc
    """)
    List<DtbRewardPenalty> findByUserAndFilter(
            UUID userId,
            LocalDate fromMonth,
            LocalDate toMonth,
            Integer type
    );

    @Query("""
        select coalesce(sum(r.amount), 0)
        from DtbRewardPenalty r
        where r.user.userId = :userId
          and r.month between :fromDate and :toDate
          and r.type = :type
    """)
    BigDecimal sumByAmountType(
        @Param("userId") UUID userId,
        @Param("fromDate") LocalDate fromDate,
        @Param("toDate") LocalDate toDate,
        @Param("type") int type
    );

    Optional<DtbRewardPenalty> findByIdAndIsDeletedFalse(UUID rewardPenaltyId);
}

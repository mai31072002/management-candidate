package com.candidate.candidate_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "dtb_rewards_penalties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtbRewardPenalty extends EntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "employees_id", nullable = false)
//    private DtbEmployees employee;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private DtbUser user;

    @Column(nullable = false)
    private LocalDate month; // "2025-01"

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount; // số tiền thưởng

    @Column(nullable = false)
    private int type; // mặc định 0: Thưởng, 1: nghỉ khoản phạt

    private String reason; // lý do thưởng/phạt
}


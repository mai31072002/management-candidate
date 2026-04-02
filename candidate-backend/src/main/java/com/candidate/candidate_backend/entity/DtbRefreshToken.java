package com.candidate.candidate_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class DtbRefreshToken {
    @Id
    private String tokenId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private DtbUser user; // refreshToken cho User nào
    private Instant expiryDate; // Thời gian sống của refreshToken
    private boolean revoked; // Kiểm tra refresh đã thu hồi chữa -> logout
}

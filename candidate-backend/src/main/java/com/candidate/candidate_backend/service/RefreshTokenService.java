package com.candidate.candidate_backend.service;

import com.candidate.candidate_backend.entity.DtbRefreshToken;
import com.candidate.candidate_backend.entity.DtbUser;
import com.candidate.candidate_backend.repositorry.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public DtbRefreshToken createRefreshToken(DtbUser dtbUser) {
        DtbRefreshToken dtbRefreshToken = new DtbRefreshToken();

        dtbRefreshToken.setTokenId(UUID.randomUUID().toString());
        dtbRefreshToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
        dtbRefreshToken.setRevoked(false);
        dtbRefreshToken.setUser(dtbUser);

        return refreshTokenRepository.save(dtbRefreshToken);
    }

    public DtbRefreshToken verifyRefreshToken(String token) {
        DtbRefreshToken dtbRefreshToken = refreshTokenRepository
                .findByTokenIdAndRevokedFalse(token).orElseThrow(
                        ()-> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token không hợp lệ")
                );

        // Kiểm tra token đã hết hạn chưa
        if (dtbRefreshToken.getExpiryDate().isBefore(Instant.now())) {
            dtbRefreshToken.setRevoked(true); // -> nếu hết hạn lưu true
            refreshTokenRepository.save(dtbRefreshToken);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token đã hết hạn");
        }

        return dtbRefreshToken;
    }
}

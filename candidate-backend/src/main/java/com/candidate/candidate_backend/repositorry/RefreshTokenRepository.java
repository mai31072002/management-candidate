package com.candidate.candidate_backend.repositorry;

import com.candidate.candidate_backend.entity.DtbRefreshToken;
import com.candidate.candidate_backend.entity.DtbUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<DtbRefreshToken, String> {
    Optional<DtbRefreshToken> findByTokenIdAndRevokedFalse(String tokenId);

    void deleteAllByUser(DtbUser user);
}

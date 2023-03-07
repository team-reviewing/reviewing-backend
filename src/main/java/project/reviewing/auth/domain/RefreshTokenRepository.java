package project.reviewing.auth.domain;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(final RefreshToken refreshToken);
    Optional<RefreshToken> findByTokenString(final String tokenString);
    void delete(final RefreshToken refreshToken);
    void deleteById(final Long memberId);
}

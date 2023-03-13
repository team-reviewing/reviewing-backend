package project.reviewing.auth.domain;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(final RefreshToken refreshToken);
    Optional<RefreshToken> findById(final Long id);
    void deleteById(final Long id);
}

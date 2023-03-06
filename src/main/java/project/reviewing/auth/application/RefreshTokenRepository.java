package project.reviewing.auth.application;

import project.reviewing.auth.domain.RefreshToken;

public interface RefreshTokenRepository {
    RefreshToken save(final RefreshToken refreshToken);
}

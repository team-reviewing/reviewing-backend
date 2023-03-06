package project.reviewing.auth.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import project.reviewing.auth.application.RefreshTokenRepository;
import project.reviewing.auth.domain.RefreshToken;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long>, RefreshTokenRepository {
}

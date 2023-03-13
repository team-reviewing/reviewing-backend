package project.reviewing.auth.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface RefreshTokenRepository extends Repository<RefreshToken, Long> {

    RefreshToken save(RefreshToken entity);
    Optional<RefreshToken> findById(Long id);
    void deleteById(Long id);
}

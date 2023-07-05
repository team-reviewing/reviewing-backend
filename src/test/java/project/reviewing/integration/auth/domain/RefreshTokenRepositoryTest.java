package project.reviewing.integration.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.domain.RefreshTokenRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("RefreshTokenRepository는 ")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RefreshTokenRepositoryTest {

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @DisplayName("RefreshToken을 저장하고, 저장된 데이터는 정해진 시간 이후 삭제된다.")
    @Test
    void saveTest() throws InterruptedException {
        RefreshToken refreshToken1 = new RefreshToken(1L, "tokenString", 1000L);
        RefreshToken refreshToken2 = new RefreshToken(2L, "tokenString", 3000L);

        refreshTokenRepository.save(refreshToken1);
        refreshTokenRepository.save(refreshToken2);

        Thread.sleep(1000L);

        assertAll(
                () -> assertThat(refreshTokenRepository.findById(1L)).isEmpty(),
                () -> assertThat(refreshTokenRepository.findById(2L)).isNotEmpty()
        );
    }
}

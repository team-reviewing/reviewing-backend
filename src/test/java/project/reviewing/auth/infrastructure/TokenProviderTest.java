package project.reviewing.auth.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.auth.exception.InvalidTokenException;
import project.reviewing.member.domain.Role;

import static org.junit.jupiter.api.Assertions.*;

public class TokenProviderTest {

    private final TokenProvider tokenProvider = new TokenProvider(
            "TBxaHGLL5S5fHtWMd70slGyAjLjE5wI16r36CiMYb4xfi07gN9UZQHai6loMQCf",
            "C1R3xJGvrjblJ48qwLqyfwbhhUUZhLxD1ju6tXmYG7o8t0S6l537WPGBnsqHnIa",
            10000L,
            20000L
    );

    @DisplayName("유저 id와 권한으로 생성한 정상적인 JWT를 검증한다.")
    @Test
    void validTokenTest() {
        String accessToken = tokenProvider.createAccessToken(1L, Role.ROLE_USER);

        assertDoesNotThrow(() -> tokenProvider.parseAccessToken(accessToken));
    }

    @DisplayName("기간이 만료된 JWT를 검증한다.")
    @Test
    void inValidTokenTest() {
        String accessToken = tokenProvider.createAccessTokenUsingTime(1L, Role.ROLE_USER, 0L);

        assertThrows(InvalidTokenException.class, () -> tokenProvider.parseAccessToken(accessToken));
    }
}

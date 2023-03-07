package project.reviewing.auth.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.auth.exception.InvalidTokenException;
import project.reviewing.member.domain.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class TokenProviderTest {

    private static final String SECRET_KEY = "TBxaHGLL5S5fHtWMd70slGyAjLjE5wI16r36CiMYb4xfi07gN9UZQHai6loMQCf";
    private static final Long ACCESS_TOKEN_VALID_TIME = 10000L;
    private static final Long REFRESH_TOKEN_VALID_TIME = 20000l;
    private final TokenProvider tokenProvider = new TokenProvider(SECRET_KEY, ACCESS_TOKEN_VALID_TIME, REFRESH_TOKEN_VALID_TIME);

    @DisplayName("유저 id와 권한으로 생성한 정상적인 JWT를 검증한다.")
    @Test
    void validTokenTest() {
        String token = tokenProvider.createJwt(1L, Role.ROLE_USER, 2000L);

        assertAll(
                () -> assertDoesNotThrow(() -> tokenProvider.parseJwt(token)),
                () -> assertThat(tokenProvider.parseJwt(token).get("id")).isEqualTo(1)
        );
    }

    @DisplayName("기간이 만료된 JWT를 검증한다.")
    @Test
    void inValidTokenTest() {
        String token = tokenProvider.createJwt(1L, Role.ROLE_USER, 0L);

        assertThrows(InvalidTokenException.class, () -> tokenProvider.parseJwt(token));
    }
}

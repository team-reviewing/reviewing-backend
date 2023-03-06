package project.reviewing.auth.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.auth.exception.InvalidTokenException;
import project.reviewing.member.domain.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TokenProviderTest {

    private static final String secretKey = "TBxaHGLL5S5fHtWMd70slGyAjLjE5wI16r36CiMYb4xfi07gN9UZQHai6loMQCf";
    private final TokenProvider tokenProvider = new TokenProvider(secretKey, 0L, 0L);

    @DisplayName("유저 id와 권한으로 생성한 정상적인 JWT를 검증한다.")
    @Test
    void validTokenTest() {
        String token = tokenProvider.createJwt(1L, Role.ROLE_USER, 2000L);

        assertDoesNotThrow(() -> tokenProvider.parseJwt(token));
        assertThat(tokenProvider.parseJwt(token).get("id")).isEqualTo(1);
    }

    @DisplayName("기간이 만료된 JWT를 검증한다.")
    @Test
    void inValidTokenTest() {
        String token = tokenProvider.createJwt(1L, Role.ROLE_USER, 0L);

        assertThrows(InvalidTokenException.class, () -> tokenProvider.parseJwt(token));
    }
}

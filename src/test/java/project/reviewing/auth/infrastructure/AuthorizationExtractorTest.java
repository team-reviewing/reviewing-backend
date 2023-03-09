package project.reviewing.auth.infrastructure;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationExtractorTest {

    @DisplayName("Request에서 Authorization header 안의 Token을 정상적으로 추출한다.")
    @Test
    void extractTokenTest() {
        final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        final String tokenString = "ALDIFE";

        httpServletRequest.addHeader("Authorization", "Bearer " + tokenString);

        Optional<String> result = AuthorizationExtractor.extract(httpServletRequest);

        assertAll(
                () -> assertThat(result).isNotEmpty(),
                () -> assertThat(result.get()).isEqualTo(tokenString)
        );
    }

    @DisplayName("Request에 Authorization header가 포함되지 않아 Token 추출을 실패한다.")
    @Test
    void extractTokenFailedForNotHeaderTest() {
        final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();

        Optional<String> result = AuthorizationExtractor.extract(httpServletRequest);

        assertThat(result).isEmpty();
    }

    @DisplayName("Request의 Authorization header에 Token이 포함되지 않아 Token 추출을 실패한다.")
    @Test
    void extractTokenFailedForNotTokenTest() {
        final MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.addHeader("Authorization", "Bearer ");

        Optional<String> result = AuthorizationExtractor.extract(httpServletRequest);

        assertThat(result).isEmpty();
    }
}

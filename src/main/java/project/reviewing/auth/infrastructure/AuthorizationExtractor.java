package project.reviewing.auth.infrastructure;

import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class AuthorizationExtractor {

    private static final int IDX_TYPE = 0;
    private static final int IDX_VALUE = 1;
    private static final String TYPE_BEARER = "Bearer";

    public static Optional<String> extract(final HttpServletRequest request) {
        final String headerValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (headerValue == null) {
            return Optional.empty();
        }
        return extractTokenString(headerValue.split(" "));
    }

    private static Optional<String> extractTokenString(final String[] values) {
        if (values.length == 2 && values[IDX_TYPE].equals(TYPE_BEARER)) {
            return Optional.of(values[IDX_VALUE]);
        }
        return Optional.empty();
    }
}

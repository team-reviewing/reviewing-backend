package project.reviewing.auth.presentation;

import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.auth.exception.InvalidTokenException;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.common.exception.ErrorType;

@RequiredArgsConstructor
@Component
public class RefreshInterceptor implements HandlerInterceptor {

    private static final String REFRESH_TOKEN = "refresh_token";

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthContext authContext;

    @Transactional
    @Override
    public boolean preHandle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler
    ) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        final String token = extractRefreshTokenString(request)
                .orElseThrow(() -> new InvalidTokenException(ErrorType.INVALID_TOKEN));

        final long id = tokenProvider.parseRefreshToken(token);

        if (isInvalidInDB(id, token)) {
            refreshTokenRepository.deleteById(id);
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN);
        }

        authContext.setId(id);
        return true;
    }

    private Optional<String> extractRefreshTokenString(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN)) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    private boolean isInvalidInDB(final long id, final String token) {
        final Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(id);
        return refreshToken.isEmpty() || !refreshToken.get().getToken().equals(token);
    }
}
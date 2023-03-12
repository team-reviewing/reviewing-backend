package project.reviewing.auth.presentation;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.exception.InvalidTokenException;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.common.util.CookieType;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class RefreshInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    @Override
    public boolean preHandle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler
    ) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        final String tokenString = extractRefreshTokenString(request)
                .orElseThrow(() -> new InvalidTokenException(ErrorType.INVALID_TOKEN));

        final Claims claims = tokenProvider.parseRefreshToken(tokenString);
        final Long id = (long) (int) claims.get("id");

        if (isInvalidInDB(id, tokenString)) {
            refreshTokenRepository.deleteById(id);
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN);
        }

        request.setAttribute("id", id);
        return true;
    }

    private Optional<String> extractRefreshTokenString(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CookieType.REFRESH_TOKEN.getValue())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    private boolean isInvalidInDB(final Long id, final String tokenString) {
        final Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(id);
        return refreshToken.isEmpty() || !refreshToken.get().getTokenString().equals(tokenString);
    }
}

package project.reviewing.auth.presentation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.exception.RefreshTokenException;
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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String tokenString = findRefreshTokenString(request.getCookies())
                .orElseThrow(() -> new RefreshTokenException(ErrorType.INVALID_TOKEN));

        final RefreshToken refreshToken = refreshTokenRepository.findByTokenString(tokenString)
                .orElseThrow(() -> new RefreshTokenException(ErrorType.INVALID_TOKEN));

        if (!refreshToken.getTokenString().equals(tokenString)) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException(ErrorType.INVALID_TOKEN);
        }

        final Claims claims = parseRefreshToken(tokenString);
        request.setAttribute("id", claims.get("id"));
        request.setAttribute("role", claims.get("role"));
        return true;
    }

    private Claims parseRefreshToken(final String tokenString) {
        try {
            return tokenProvider.parseJwt(tokenString);
        } catch(JwtException e) {
            throw new RefreshTokenException(ErrorType.INVALID_TOKEN);
        }
    }

    private Optional<String> findRefreshTokenString(final Cookie[] cookies) {
        if (cookies.length == 0) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CookieType.REFRESH_TOKEN)) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}

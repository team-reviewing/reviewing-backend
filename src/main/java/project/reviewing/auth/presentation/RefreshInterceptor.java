package project.reviewing.auth.presentation;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.exception.RefreshTokenException;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.common.exception.ErrorType;

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
                .orElseThrow(() -> new RefreshTokenException(ErrorType.INVALID_TOKEN));
        final RefreshToken refreshToken = refreshTokenRepository.findByTokenString(tokenString)
                .orElseThrow(() -> new RefreshTokenException(ErrorType.INVALID_TOKEN));

        if (!refreshToken.getTokenString().equals(tokenString)) {
            refreshTokenRepository.delete(refreshToken);
            throw new RefreshTokenException(ErrorType.INVALID_TOKEN);
        }

        final Claims claims = tokenProvider.parseRefreshToken(tokenString);

        request.setAttribute("id", claims.get("id"));
        request.setAttribute("role", claims.get("role"));
        return true;
    }

    private Optional<String> extractRefreshTokenString(final HttpServletRequest request) {
        return Optional.of(request.getParameter("refresh_token"));
    }
}

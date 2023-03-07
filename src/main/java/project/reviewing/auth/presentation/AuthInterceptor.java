package project.reviewing.auth.presentation;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
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
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final String accessToken = findAccessToken(request.getCookies())
                .orElseThrow(() -> new InvalidTokenException(ErrorType.INVALID_TOKEN));

        final Claims claims = tokenProvider.parseAccessToken(accessToken);

        request.setAttribute("id", claims.get("id"));
        return true;
    }

    private Optional<String> findAccessToken(final Cookie[] cookies) {
        if (cookies.length == 0) {
            return Optional.empty();
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(CookieType.ACCESS_TOKEN)) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}

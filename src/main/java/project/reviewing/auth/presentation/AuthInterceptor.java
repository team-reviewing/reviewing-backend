package project.reviewing.auth.presentation;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import project.reviewing.auth.exception.InvalidTokenException;
import project.reviewing.auth.infrastructure.AuthorizationExtractor;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.common.exception.ErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;
    private final AuthContext authContext;

    @Override
    public boolean preHandle(
            final HttpServletRequest request, final HttpServletResponse response, final Object handler
    ) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }

        final String accessToken = AuthorizationExtractor.extract(request)
                .orElseThrow(() -> new InvalidTokenException(ErrorType.INVALID_TOKEN));

        authContext.setId(tokenProvider.parseAccessToken(accessToken));
        return true;
    }
}

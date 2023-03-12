package project.reviewing.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.application.AuthService;
import project.reviewing.auth.application.response.LoginGithubResponse;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.auth.presentation.response.AccessTokenResponse;
import project.reviewing.common.util.CookieProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping(value = "/login/github")
    ResponseEntity<AccessTokenResponse> oauthloginGithub(
            @RequestBody @NotBlank final String authorizationCode, final HttpServletResponse response
    ) {
        LoginGithubResponse loginGithubResponse = authService.loginGithub(authorizationCode);

        final AccessTokenResponse accessTokenResponse = new AccessTokenResponse(loginGithubResponse.getAccessToken());

        response.addCookie(
                CookieProvider.createRefreshTokenCookie(
                        loginGithubResponse.getRefreshToken(), tokenProvider.getRefreshTokenValidTime()
                )
        );
        return ResponseEntity.ok(accessTokenResponse);
    }

    @PostMapping(value = "/refresh")
    ResponseEntity<AccessTokenResponse> refreshTokens(
            final HttpServletRequest request, final HttpServletResponse response
    ) {
        RefreshResponse refreshResponse = authService.refreshTokens((Long) request.getAttribute("id"));

        final AccessTokenResponse accessTokenResponse = new AccessTokenResponse(refreshResponse.getAccessToken());

        response.addCookie(
                CookieProvider.createRefreshTokenCookie(
                        refreshResponse.getRefreshToken(), tokenProvider.getRefreshTokenValidTime()
                )
        );
        return ResponseEntity.ok(accessTokenResponse);
    }

    @DeleteMapping(value = "/logout")
    ResponseEntity<?> logout(final HttpServletRequest request) {
        authService.removeRefreshToken((long) (int) request.getAttribute("id"));
        return ResponseEntity.noContent().build();
    }
}

package project.reviewing.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.application.AuthService;
import project.reviewing.auth.application.response.GithubLoginResponse;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.auth.presentation.response.AccessTokenResponse;
import project.reviewing.common.util.CookieProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.net.URI;

@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping(value = "/login/github")
    ResponseEntity<AccessTokenResponse> oauthGithubLogin(@RequestBody @NotBlank final String authorizationCode,
                                                         final HttpServletResponse response) {
        GithubLoginResponse githubLoginResponse = authService.githubLogin(authorizationCode);

        final AccessTokenResponse accessTokenResponse = new AccessTokenResponse(githubLoginResponse.getAccessToken());

        response.addCookie(
                CookieProvider.createRefreshTokenCookie(
                        githubLoginResponse.getRefreshToken(), tokenProvider.getRefreshTokenValidTime()
                )
        );
        return githubLoginResponse.isCreated() ?
                ResponseEntity.created(URI.create("/members/" + githubLoginResponse.getMemberId())).body(accessTokenResponse)
                : ResponseEntity.ok(accessTokenResponse);
    }

    @PostMapping(value = "/refresh")
    ResponseEntity<AccessTokenResponse> refreshTokens(final HttpServletRequest request, final HttpServletResponse response) {
        RefreshResponse refreshResponse = authService.refreshTokens((Long) request.getAttribute("id"));

        final AccessTokenResponse accessTokenResponse = new AccessTokenResponse(refreshResponse.getAccessToken());

        response.addCookie(
                CookieProvider.createRefreshTokenCookie(
                        refreshResponse.getRefreshToken(), tokenProvider.getRefreshTokenValidTime()
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(accessTokenResponse);
    }

    @DeleteMapping(value = "/logout")
    ResponseEntity<?> logout(final HttpServletRequest request) {
        authService.removeRefreshToken((long) (int) request.getAttribute("id"));
        return ResponseEntity.noContent().build();
    }
}

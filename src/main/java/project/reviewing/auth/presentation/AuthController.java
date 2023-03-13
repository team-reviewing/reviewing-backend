package project.reviewing.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.application.AuthService;
import project.reviewing.auth.application.response.GithubLoginResponse;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.auth.presentation.response.AccessTokenResponse;
import project.reviewing.common.util.CookieProvider;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

@Validated
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/login/github")
    public AccessTokenResponse oauthloginGithub(
            @RequestBody @NotBlank final String authorizationCode, final HttpServletResponse response
    ) {
        GithubLoginResponse githubLoginResponse = authService.loginGithub(authorizationCode);

        response.addCookie(
                CookieProvider.createRefreshTokenCookie(
                        githubLoginResponse.getRefreshToken(), tokenProvider.getRefreshTokenValidTime()
                )
        );
        return new AccessTokenResponse(githubLoginResponse.getAccessToken());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/refresh")
    public AccessTokenResponse refreshTokens(
            @AuthenticatedMember  final Long memberId, final HttpServletResponse response
    ) {
        RefreshResponse refreshResponse = authService.refreshTokens(memberId);

        response.addCookie(
                CookieProvider.createRefreshTokenCookie(
                        refreshResponse.getRefreshToken(), tokenProvider.getRefreshTokenValidTime()
                )
        );
        return new AccessTokenResponse(refreshResponse.getAccessToken());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/logout")
    public void logout(@AuthenticatedMember  final Long memberId) {
        authService.removeRefreshToken(memberId);
    }
}

package project.reviewing.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.application.AuthService;
import project.reviewing.auth.application.response.GithubLoginResponse;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.auth.presentation.request.GithubLoginRequest;
import project.reviewing.auth.presentation.response.LoginResponse;
import project.reviewing.common.util.CookieProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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
    public LoginResponse loginGithub(
            @Valid @RequestBody final GithubLoginRequest githubLoginRequest, final HttpServletResponse response
    ) {
        GithubLoginResponse githubLoginResponse = authService.loginGithub(githubLoginRequest.getAuthorizationCode());

        /*ResponseCookie cookie = ResponseCookie.from("refresh_token", githubLoginResponse.getRefreshToken())
                .path("/")
                .httpOnly(false)
                .sameSite("")
                .secure(false)
                .maxAge(tokenProvider.getRefreshTokenValidTime())
                .build();
        response.setHeader("Set-Cookie", cookie.toString());*/

        /*response.addCookie(
                CookieProvider.createRefreshTokenCookie(
                        githubLoginResponse.getRefreshToken(), tokenProvider.getRefreshTokenValidTime()
                )
        );*/
        return new LoginResponse(githubLoginResponse.getAccessToken(), githubLoginResponse.getRefreshToken());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/cookietest")
    public void cookietest(
            final HttpServletResponse response
    ) {

        ResponseCookie cookie = ResponseCookie.from("refresh_token", "refreshToken")
                //.domain("localhost")
                .path("/auth/cookietestresponse")
                .httpOnly(false)
                .sameSite("")
                .secure(false)
                .maxAge(tokenProvider.getRefreshTokenValidTime())
                .build();
        response.setHeader("Set-Cookie", cookie.toString());
        //System.out.println(response.getHeader("Set-Cookie"));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/cookietestresponse")
    public String cookietestresponse(
            final HttpServletRequest request
    ) {
        if (request.getCookies() == null) {
            return "empty";
        }
        return request.getCookies()[0].getName() + "=" + request.getCookies()[0].getValue();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/refresh")
    public LoginResponse refreshTokens(
            @AuthenticatedMember final Long memberId, final HttpServletResponse response
    ) {
        RefreshResponse refreshResponse = authService.refreshTokens(memberId);

        /*ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshResponse.getRefreshToken())
                .path("/auth/refresh")
                .httpOnly(false)
                .sameSite("None")
                .secure(true)
                .maxAge(tokenProvider.getRefreshTokenValidTime())
                .build();
        response.setHeader("Set-Cookie", cookie.toString());*/

        /*response.addCookie(
                CookieProvider.createRefreshTokenCookie(
                        refreshResponse.getRefreshToken(), tokenProvider.getRefreshTokenValidTime()
                )
        );*/
        return new LoginResponse(refreshResponse.getAccessToken(), refreshResponse.getRefreshToken());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/logout")
    public void logout(@AuthenticatedMember final Long memberId) {
        authService.removeRefreshToken(memberId);
    }
}

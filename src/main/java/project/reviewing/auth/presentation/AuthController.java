package project.reviewing.auth.presentation;

import static project.reviewing.common.util.CookieBuilder.NAME_ACCESS_TOKEN;
import static project.reviewing.common.util.CookieBuilder.NAME_REFRESH_TOKEN;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.application.AuthService;
import project.reviewing.auth.application.response.LoginResponse;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.common.util.CookieBuilder;

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
    ResponseEntity<?> githubLogin(
            @RequestBody @NotBlank final String authorizationCode,
            final HttpServletResponse response
    ) {
        LoginResponse loginResponse = authService.githubLogin(authorizationCode);

        response.addCookie(CookieBuilder.builder(NAME_ACCESS_TOKEN, loginResponse.getAccessToken())
                .maxAge((int) tokenProvider.getAccessTokenValidTime())
                .path("/")
                .httpOnly(true)
                .build()
        );
        response.addCookie(CookieBuilder.builder(NAME_REFRESH_TOKEN, loginResponse.getRefreshToken())
                .maxAge((int) tokenProvider.getRefreshTokenValidTime())
                .path("/auth/refresh")
                .httpOnly(true)
                .build()
        );
        return loginResponse.isCreated() ?
                ResponseEntity.created(URI.create("/members/" + loginResponse.getMemberId())).build()
                : ResponseEntity.ok().build();
    }
}

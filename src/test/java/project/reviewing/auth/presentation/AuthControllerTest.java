package project.reviewing.auth.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import project.reviewing.auth.application.response.GithubLoginResponse;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.common.ControllerTest;
import project.reviewing.common.util.CookieType;

import javax.servlet.http.Cookie;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthControllerTest extends ControllerTest {

    @DisplayName("authorization code의 공백 여부를 검증한다.")
    @Test
    void validationTest() throws Exception {
        final String authorizationCode = " ";
        final GithubLoginResponse githubLoginResponse = new GithubLoginResponse(1L, "Access Token", "Refresh Token");

        given(authService.loginGithub(authorizationCode)).willReturn(githubLoginResponse);

        mockMvc.perform(post("/auth/login/github")
                        .content(authorizationCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("login을 하면 Access Token과 Refresh Token을 반환한다.")
    @Test
    void loginTest() throws Exception {
        final String authorizationCode = "code";
        final GithubLoginResponse githubLoginResponse = new GithubLoginResponse(1L, "Access Token", "Refresh Token");

        given(authService.loginGithub(authorizationCode)).willReturn(githubLoginResponse);

        mockMvc.perform(post("/auth/login/github")
                        .content(authorizationCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(cookie().value("refresh_token", "Refresh Token"))
                .andExpect(cookie().httpOnly("refresh_token", true))
                .andExpect(cookie().path("refresh_token", "/auth/refresh"))
                .andDo(print());
    }

    @DisplayName("Access Token 재발급을 요청하면 새로운 Access Token과 Refresh Token이 반환된다.")
    @Test
    void refreshTest() throws Exception {
        final Long memberId = 1L;
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(memberId);
        final RefreshResponse newRefreshResponse = new RefreshResponse("New Access Token", "New Refresh Token");

        given(authService.refreshTokens(memberId)).willReturn(newRefreshResponse);
        given(refreshTokenRepository.findById(refreshToken.getId())).willReturn(Optional.of(refreshToken));

        mockMvc.perform(post("/auth/refresh")
                        .cookie(new Cookie(CookieType.REFRESH_TOKEN.getValue(), refreshToken.getToken())))
                .andExpect(status().isOk())
                .andExpect(cookie().value("refresh_token", "New Refresh Token"))
                .andExpect(cookie().httpOnly("refresh_token", true))
                .andExpect(cookie().path("refresh_token", "/auth/refresh"))
                .andDo(print());
    }

    @DisplayName("만료된 Refresh Token으로 Access Token을 재발급 받으면 401 반환한다.")
    @Test
    void refreshTokenExpirationTest() throws Exception {
        final Long memberId = 1L;
        final RefreshToken refreshToken = new RefreshToken(
                memberId, tokenProvider.createRefreshTokenStringUsingTime(memberId, 0L), 0L
        );
        final RefreshResponse newRefreshResponse = new RefreshResponse("New Access Token", "New Refresh Token");

        given(authService.refreshTokens(memberId)).willReturn(newRefreshResponse);
        given(refreshTokenRepository.findById(refreshToken.getId())).willReturn(Optional.of(refreshToken));

        mockMvc.perform(post("/auth/refresh")
                        .cookie(new Cookie(CookieType.REFRESH_TOKEN.getValue(), refreshToken.getToken())))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("DB에 없는(유효하지 않은) Refresh Token으로 Access Token을 재발급 받으면 401 반환한다.")
    @Test
    void refreshTokenNotInDBTest() throws Exception {
        final Long memberId = 1L;
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(memberId);
        final RefreshResponse newRefreshResponse = new RefreshResponse("New Access Token", "New Refresh Token");

        given(authService.refreshTokens(memberId)).willReturn(newRefreshResponse);
        given(refreshTokenRepository.findById(refreshToken.getId())).willReturn(Optional.empty());

        mockMvc.perform(post("/auth/refresh")
                        .cookie(new Cookie(CookieType.REFRESH_TOKEN.getValue(), refreshToken.getToken())))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("유효한 Access Token으로 logout 한다.")
    @Test
    void logoutTest() throws Exception {
        final Long memberId = 1L;
        final String accessToken = tokenProvider.createAccessToken(memberId);

        mockMvc.perform(delete("/auth/logout")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @DisplayName("만료된 Access Token으로 logout 하면 401 반환한다.")
    @Test
    void accessTokenExpirationTest() throws Exception {
        final Long memberId = 1L;
        String accessToken = tokenProvider.createAccessTokenUsingTime(memberId, 0L);

        mockMvc.perform(delete("/auth/logout")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}

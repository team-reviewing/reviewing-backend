package project.reviewing.unit.auth.presentation;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.MediaType;
import project.reviewing.auth.application.response.GithubLoginResponse;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.presentation.request.GithubLoginRequest;
import project.reviewing.unit.ControllerTest;

public class AuthControllerTest extends ControllerTest {

    @DisplayName("authorizationCode 값이 null, empty, blank이면 400 반환한다.")
    @ValueSource(strings = " ")
    @NullAndEmptySource
    @ParameterizedTest
    void validationTest(final String authorizationCode) throws Exception {
        final GithubLoginRequest request = new GithubLoginRequest(authorizationCode);

        mockMvc.perform(post("/auth/login/github")
                        .content(objectMapper.writeValueAsString(request))
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
                        .content(objectMapper.writeValueAsString(new GithubLoginRequest(authorizationCode)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
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
                        .header("Authorization", "Bearer " + refreshToken.getToken()))
                .andExpect(status().isOk())
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
                        .header("Authorization", "Bearer " + refreshToken.getToken()))
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
                        .header("Authorization", "Bearer " + refreshToken.getToken()))
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

package project.reviewing.auth.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import project.reviewing.auth.application.response.LoginResponse;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.common.ControllerTest;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerTest {

    @DisplayName("authorization code의 공백 여부를 검증한다.")
    @Test
    void validationTest() throws Exception {
        // given
        final String authorizationCode = " ";
        final LoginResponse loginResponse = new LoginResponse(1L, "Access Token", "Refresh Token", true);

        given(authService.githubLogin(authorizationCode)).willReturn(loginResponse);

        // when then
        mockMvc.perform(post("/auth/login/github")
                        .content(authorizationCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("login을 하면 Access Token과 Refresh Token을 cookie로 반환한다.")
    @Test
    void loginTest() throws Exception {
        // given
        final String authorizationCode = "code";
        final LoginResponse loginResponse = new LoginResponse(1L, "Access Token", "Refresh Token", true);

        given(authService.githubLogin(authorizationCode)).willReturn(loginResponse);

        // when then
        mockMvc.perform(post("/auth/login/github")
                        .content(authorizationCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(cookie().value("access_token", "Access Token"))
                .andExpect(cookie().httpOnly("access_token", true))
                .andExpect(cookie().value("refresh_token", "Refresh Token"))
                .andExpect(cookie().httpOnly("refresh_token", true))
                .andExpect(cookie().path("refresh_token", "/auth/refresh"))
                .andDo(print());
    }

    @DisplayName("Access Token 재발급을 요청하면 새로운 Access Token과 Refresh Token이 cookie로 반환된다.")
    @Test
    void refreshTest() throws Exception {
        // given
        final Long memberId = 1L;
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(memberId);
        final RefreshResponse newRefreshResponse = new RefreshResponse("New Access Token", "New Refresh Token");

        given(authService.refreshTokens(memberId)).willReturn(newRefreshResponse);
        given(refreshTokenRepository.findById(refreshToken.getId())).willReturn(Optional.of(refreshToken));

        // when then
        mockMvc.perform(post("/auth/refresh?refresh_token=" + refreshToken.getTokenString()))
                .andExpect(status().isCreated())
                .andExpect(cookie().value("access_token", "New Access Token"))
                .andExpect(cookie().httpOnly("access_token", true))
                .andExpect(cookie().value("refresh_token", "New Refresh Token"))
                .andExpect(cookie().httpOnly("refresh_token", true))
                .andExpect(cookie().path("refresh_token", "/auth/refresh"))
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
}

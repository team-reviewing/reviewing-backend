package project.reviewing.auth.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.common.ControllerTest;
import project.reviewing.member.domain.Role;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RefreshInterceptorTest extends ControllerTest {

    @DisplayName("유효한 Refresh Token으로 Access Token을 재발급 받는다.")
    @Test
    void refreshTest() throws Exception {
        // given
        final Long memberId = 1L;
        final Role role = Role.ROLE_USER;
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(memberId, role);
        final RefreshResponse newRefreshResponse = new RefreshResponse("New Access Token", "New Refresh Token");

        given(authService.refreshTokens(memberId, role))
                .willReturn(newRefreshResponse);
        given(refreshTokenRepository.findById(refreshToken.getId()))
                .willReturn(Optional.of(refreshToken));

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

    @DisplayName("만료된 Refresh Token으로 Access Token을 재발급 받으면 실패한다.")
    @Test
    void refreshTokenExpirationTest() throws Exception {
        // given
        final Long memberId = 1L;
        final Role role = Role.ROLE_USER;
        final RefreshToken refreshToken = new RefreshToken(
                memberId, tokenProvider.createRefreshTokenUsingTime(memberId, role, 0L), 0L
        );
        final RefreshResponse newRefreshResponse = new RefreshResponse("New Access Token", "New Refresh Token");

        given(authService.refreshTokens(memberId, role))
                .willReturn(newRefreshResponse);
        given(refreshTokenRepository.findById(refreshToken.getId()))
                .willReturn(Optional.of(refreshToken));

        // when then
        mockMvc.perform(post("/auth/refresh?refresh_token=" + refreshToken.getTokenString()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("DB에 없는(유효하지 않은) Refresh Token으로 Access Token을 재발급 받으면 실패한다.")
    @Test
    void refreshTokenNotInDBTest() throws Exception {
        // given
        final Long memberId = 1L;
        final Role role = Role.ROLE_USER;
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(memberId, role);
        final RefreshResponse newRefreshResponse = new RefreshResponse("New Access Token", "New Refresh Token");

        given(authService.refreshTokens(memberId, role))
                .willReturn(newRefreshResponse);
        given(refreshTokenRepository.findById(refreshToken.getId()))
                .willReturn(Optional.empty());

        // when then
        mockMvc.perform(post("/auth/refresh?refresh_token=" + refreshToken.getTokenString()))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}

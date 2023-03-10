package project.reviewing.auth.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.presentation.response.AccessTokenResponse;
import project.reviewing.common.ControllerTest;
import project.reviewing.common.util.CookieType;

import javax.servlet.http.Cookie;
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
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(memberId);
        final RefreshResponse newRefreshResponse = new RefreshResponse("New Access Token", "New Refresh Token");
        final AccessTokenResponse expectedAccessTokenResponse = new AccessTokenResponse("New Access Token");

        given(authService.refreshTokens(memberId)).willReturn(newRefreshResponse);
        given(refreshTokenRepository.findById(refreshToken.getId())).willReturn(Optional.of(refreshToken));

        // when then
        mockMvc.perform(post("/auth/refresh")
                        .cookie(new Cookie(CookieType.REFRESH_TOKEN, refreshToken.getTokenString())))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedAccessTokenResponse)))
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
        final RefreshToken refreshToken = new RefreshToken(
                memberId, tokenProvider.createRefreshTokenStringUsingTime(memberId, 0L), 0L
        );
        final RefreshResponse newRefreshResponse = new RefreshResponse("New Access Token", "New Refresh Token");

        given(authService.refreshTokens(memberId)).willReturn(newRefreshResponse);
        given(refreshTokenRepository.findById(refreshToken.getId())).willReturn(Optional.of(refreshToken));

        // when then
        mockMvc.perform(post("/auth/refresh")
                        .cookie(new Cookie(CookieType.REFRESH_TOKEN, refreshToken.getTokenString())))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @DisplayName("DB에 없는(유효하지 않은) Refresh Token으로 Access Token을 재발급 받으면 실패한다.")
    @Test
    void refreshTokenNotInDBTest() throws Exception {
        // given
        final Long memberId = 1L;
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(memberId);
        final RefreshResponse newRefreshResponse = new RefreshResponse("New Access Token", "New Refresh Token");

        given(authService.refreshTokens(memberId)).willReturn(newRefreshResponse);
        given(refreshTokenRepository.findById(refreshToken.getId())).willReturn(Optional.empty());

        // when then
        mockMvc.perform(post("/auth/refresh")
                        .cookie(new Cookie(CookieType.REFRESH_TOKEN, refreshToken.getTokenString())))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}

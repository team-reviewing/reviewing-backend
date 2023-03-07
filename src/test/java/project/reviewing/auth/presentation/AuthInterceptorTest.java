package project.reviewing.auth.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import project.reviewing.auth.application.response.LoginResponse;
import project.reviewing.common.ControllerTest;
import project.reviewing.common.util.CookieType;
import project.reviewing.member.application.response.MemberResponse;
import project.reviewing.member.domain.Role;

import javax.servlet.http.Cookie;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthInterceptorTest extends ControllerTest {

    @DisplayName("정상적인 Access Token으로 API를 호출한다.")
    @Test
    void accessTokenTest() throws Exception {
        // given
        final String authorizationCode = "code";
        String accessToken = tokenProvider.createAccessTokenUsingTime(1L, Role.ROLE_USER, 10000L);
        final LoginResponse loginResponse = new LoginResponse(1L, accessToken, "Refresh Token", true);

        given(authService.githubLogin(authorizationCode))
                .willReturn(loginResponse);

        // when then
        mockMvc.perform(post("/auth/login/github")
                        .content(authorizationCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie(CookieType.ACCESS_TOKEN, accessToken))
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/members/1"))
                .andDo(print());
    }

    @DisplayName("Access Token 만료 상태에서 유저 정보 조회 API 호출 시 거부한다.")
    @Test
    void accessTokenExpirationTest() throws Exception {
        // given
        final Long memgerId = 1L;
        String accessToken = tokenProvider.createAccessTokenUsingTime(memgerId, Role.ROLE_USER, 0L);
        final MemberResponse memberResponse = new MemberResponse("image_url");

        given(memberService.findMemberProfile(memgerId))
                .willReturn(memberResponse);

        // when then
        mockMvc.perform(get("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie(CookieType.ACCESS_TOKEN, accessToken))
                        .characterEncoding("UTF-8"))
                .andExpect(status().isUnauthorized())
                .andExpect(header().exists("WWW-Authenticated"))
                .andExpect(header().string("WWW-Authenticated", "Basic realm=\"/auth/refresh\""))
                .andDo(print());
    }
}

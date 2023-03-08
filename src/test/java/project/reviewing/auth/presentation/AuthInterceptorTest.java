package project.reviewing.auth.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.common.ControllerTest;
import project.reviewing.member.application.response.MemberResponse;
import project.reviewing.member.domain.Role;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthInterceptorTest extends ControllerTest {

    @DisplayName("정상적인 Access Token으로 API를 호출한다.")
    @Test
    void accessTokenTest() throws Exception {
        // given
        final Long memberId = 1L;
        String accessToken = tokenProvider.createAccessTokenUsingTime(memberId, Role.ROLE_USER, 10000L);
        final MemberResponse memberResponse = new MemberResponse("image_url");

        given(memberService.findMemberProfile(memberId))
                .willReturn(memberResponse);

        // when then
        mockMvc.perform(get("/members")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("Access Token 만료 상태에서 유저 정보 조회 API 호출 시 거부한다.")
    @Test
    void accessTokenExpirationTest() throws Exception {
        // given
        final Long memberId = 1L;
        String accessToken = tokenProvider.createAccessTokenUsingTime(memberId, Role.ROLE_USER, 0L);
        final MemberResponse memberResponse = new MemberResponse("image_url");

        given(memberService.findMemberProfile(memberId))
                .willReturn(memberResponse);

        // when then
        mockMvc.perform(get("/members")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}

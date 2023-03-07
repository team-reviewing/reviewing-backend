package project.reviewing.member.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import project.reviewing.common.ControllerTest;
import project.reviewing.common.util.CookieType;
import project.reviewing.member.application.response.MemberResponse;
import project.reviewing.member.domain.Role;

import javax.servlet.http.Cookie;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends ControllerTest {

    @DisplayName("유저 기본 정보를 가져온다.")
    @Test
    void findMemberProfileTest() throws Exception {
        // given
        final Long memberId = 1L;
        final String imageURL = "image_url";
        final String accessToken = tokenProvider.createAccessToken(memberId, Role.ROLE_USER);
        final MemberResponse memberResponse = new MemberResponse(imageURL);

        final String expectedResult = "{" +
                "\"image_url\": " + imageURL +
                "}";

        given(memberService.findMemberProfile(memberId))
                .willReturn(memberResponse);

        // when then
        mockMvc.perform(get("/members")
                        .cookie(new Cookie(CookieType.ACCESS_TOKEN, accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult))
                .andDo(print());
    }
}

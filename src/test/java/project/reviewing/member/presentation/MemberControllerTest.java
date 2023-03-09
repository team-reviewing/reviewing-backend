package project.reviewing.member.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.common.ControllerTest;
import project.reviewing.member.application.response.MemberResponse;

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
        final String accessToken = tokenProvider.createAccessToken(memberId);
        final MemberResponse memberResponse = new MemberResponse(imageURL);
        final String expectedResult = "{" +
                "\"image_url\": " + imageURL +
                "}";

        given(memberService.findMemberProfile(memberId)).willReturn(memberResponse);

        // when then
        mockMvc.perform(get("/members")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResult))
                .andDo(print());
    }
}

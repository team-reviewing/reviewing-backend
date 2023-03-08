package project.reviewing.unit.member.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestController;
import project.reviewing.member.command.application.MemberService;
import project.reviewing.member.command.application.request.UpdatingMemberRequest;
import project.reviewing.member.query.application.MemberQueryService;

@DisplayName("MemberController 는 ")
@WebMvcTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = RestController.class))
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberQueryService memberQueryService;

    @DisplayName("내 정보 수정 시")
    @Nested
    class MemberUpdateTest {

        @DisplayName("정상적으로 내 정보를 수정하는 경우 204를 반환한다.")
        @Test
        void updateMyInformation() throws Exception {
            final UpdatingMemberRequest request = new UpdatingMemberRequest("username", "email@gmail.com");

            mockMvc.perform(patch("/members/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @DisplayName("username에 null 또는 빈 값이 있는 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void updateMemberWithInvalidUsername(final String username) throws Exception {
            final UpdatingMemberRequest request = new UpdatingMemberRequest(username, "email@gmail.com");

            mockMvc.perform(patch("/members/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException()
                                    .getClass()
                                    .isAssignableFrom(MethodArgumentNotValidException.class)
                            ).isTrue()
                    );
        }

        @DisplayName("email에 null 또는 빈 값이 있는 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void updateMemberWithInvalidEmail(final String email) throws Exception {
            final UpdatingMemberRequest request = new UpdatingMemberRequest("username", email);

            mockMvc.perform(patch("/members/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException()
                                    .getClass()
                                    .isAssignableFrom(MethodArgumentNotValidException.class)
                            ).isTrue()
                    );
        }
    }
}

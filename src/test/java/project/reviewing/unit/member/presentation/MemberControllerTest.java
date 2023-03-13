package project.reviewing.unit.member.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import project.reviewing.member.command.application.request.MyInformationUpdateRequest;
import project.reviewing.member.command.application.request.ReviewerRegistrationRequest;
import project.reviewing.member.command.application.request.ReviewerUpdateRequest;
import project.reviewing.unit.ControllerTest;

@DisplayName("MemberController 는 ")
public class MemberControllerTest extends ControllerTest {

    @DisplayName("내 정보 수정 시")
    @Nested
    class MemberUpdateTest {

        @DisplayName("정상적으로 내 정보를 수정하는 경우 204를 반환한다.")
        @Test
        void updateMyInformation() throws Exception {
            final String token = "Bearer " + tokenProvider.createAccessToken(1L);
            final MyInformationUpdateRequest request = new MyInformationUpdateRequest("username", "email@gmail.com");

            willDoNothing().given(memberService).update(anyLong(), any());

            mockMvc.perform(patch("/members/me")
                            .header(HttpHeaders.AUTHORIZATION, token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @DisplayName("username에 null 또는 빈 값이 있는 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void updateMemberWithInvalidUsername(final String username) throws Exception {
            final MyInformationUpdateRequest request = new MyInformationUpdateRequest(username, "email@gmail.com");

            assertValidation(patch("/members/me"), request);
        }

        @DisplayName("email에 null 또는 빈 값이 있는 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void updateMemberWithInvalidEmail(final String email) throws Exception {
            final MyInformationUpdateRequest request = new MyInformationUpdateRequest("username", email);

            assertValidation(patch("/members/me"), request);
        }
    }

    @DisplayName("리뷰어 등록 시")
    @Nested
    class ReviewerInformationResponseRegisterTest {

        @DisplayName("직무를 입력하지 않은 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void registerReviewerWithOutJob(final String job) throws Exception {
            final ReviewerRegistrationRequest request = new ReviewerRegistrationRequest(
                    job, "career", List.of(1L, 2L), "introduce"
            );

            assertValidation(post("/members/me/reviewer"), request);
        }

        @DisplayName("경력을 입력하지 않은 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void registerReviewerWithOutCareer(final String career) throws Exception {
            final ReviewerRegistrationRequest request = new ReviewerRegistrationRequest(
                    "job", career, List.of(1L, 2L), "introduce"
            );

            assertValidation(post("/members/me/reviewer"), request);
        }

        @DisplayName("기술스택을 입력하지 않은 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void registerReviewerWithOutTechStack(final List<Long> techStack) throws Exception {
            final ReviewerRegistrationRequest request = new ReviewerRegistrationRequest(
                    "job", "career", techStack, "introduce"
            );

            assertValidation(post("/members/me/reviewer"), request);
        }

        @DisplayName("자기소개를 입력하지 않은 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void registerReviewerWithOutIntroduction(final String introduction) throws Exception {
            final ReviewerRegistrationRequest request = new ReviewerRegistrationRequest(
                    "job", "career", List.of(1L), introduction
            );

            assertValidation(post("/members/me/reviewer"), request);
        }
    }

    @DisplayName("리뷰어 수정 시")
    @Nested
    class ReviewerUpdateTest {

        @DisplayName("직무를 입력하지 않은 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void updateReviewerWithOutJob(final String job) throws Exception {
            final ReviewerUpdateRequest request = new ReviewerUpdateRequest(
                    job, "career", List.of(1L, 2L), "introduce"
            );

            assertValidation(patch("/members/me/reviewer"), request);
        }

        @DisplayName("경력을 입력하지 않은 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void updateReviewerWithOutCareer(final String career) throws Exception {
            final ReviewerUpdateRequest request = new ReviewerUpdateRequest(
                    "job", career, List.of(1L, 2L), "introduce"
            );

            assertValidation(patch("/members/me/reviewer"), request);
        }

        @DisplayName("기술스택을 입력하지 않은 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void updateReviewerWithOutTechStack(final List<Long> techStack) throws Exception {
            final ReviewerUpdateRequest request = new ReviewerUpdateRequest(
                    "job", "career", techStack, "introduce"
            );

            assertValidation(patch("/members/me/reviewer"), request);
        }

        @DisplayName("자기소개를 입력하지 않은 경우 400을 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void updateReviewerWithOutIntroduction(final String introduction) throws Exception {
            final ReviewerUpdateRequest request = new ReviewerUpdateRequest(
                    "job", "career", List.of(1L), introduction
            );

            assertValidation(patch("/members/me/reviewer"), request);
        }
    }

    private void assertValidation(final MockHttpServletRequestBuilder url, final Object request) throws Exception {
        final String token = "Bearer " + tokenProvider.createAccessToken(1L);

        mockMvc.perform(url
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
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

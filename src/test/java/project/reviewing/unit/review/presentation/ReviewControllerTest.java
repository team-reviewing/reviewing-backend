package project.reviewing.unit.review.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import project.reviewing.review.presentation.request.ReviewCreateRequest;
import project.reviewing.review.presentation.request.ReviewUpdateRequest;
import project.reviewing.unit.ControllerTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ReviewController 는 ")
public class ReviewControllerTest extends ControllerTest {

    @DisplayName("리뷰 생성 시 ")
    @Nested
    class ReviewCreateTest {

        @DisplayName("요청이 유효하면 200 반환한다.")
        @Test
        void validCreateReview() throws Exception {
            final ReviewCreateRequest request = new ReviewCreateRequest(
                    "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            requestAboutReview(post("/reviewers/1/reviews"), request)
                    .andExpect(status().isOk());
        }

        @DisplayName("title이 null, empty, blank면 400 반환한다.")
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @ParameterizedTest
        void createWithTitleNullAndEmpty(final String title) throws Exception {
            final ReviewCreateRequest request = new ReviewCreateRequest(
                    title, "본문", "https://github.com/Tom/myproject/pull/1"
            );

            requestAboutReview(post("/reviewers/1/reviews"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }

        @DisplayName("title이 제한 길이 50자보다 길면 400 반환한다.")
        @Test
        void createWithTitleGreaterThanMaxLen() throws Exception {
            final String title = makeStringByLength(51);
            final ReviewCreateRequest request = new ReviewCreateRequest(
                    title, "본문", "https://github.com/Tom/myproject/pull/1"
            );

            requestAboutReview(post("/reviewers/1/reviews"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }

        @DisplayName("content가 null, empty, blank면 400 반환한다.")
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @ParameterizedTest
        void createWithContentNullAndEmpty(final String content) throws Exception {
            final ReviewCreateRequest request = new ReviewCreateRequest(
                    "리뷰 요청합니다.", content, "https://github.com/Tom/myproject/pull/1"
            );

            requestAboutReview(post("/reviewers/1/reviews"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }

        @DisplayName("content가 제한 길이 1500자보다 길면 400 반환한다.")
        @Test
        void createWithContentGreaterThanMaxLen() throws Exception {
            final String content = makeStringByLength(1501);
            final ReviewCreateRequest request = new ReviewCreateRequest(
                    "리뷰 요청합니다.", content, "https://github.com/Tom/myproject/pull/1"
            );

            requestAboutReview(post("/reviewers/1/reviews"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }

        @DisplayName("prUrl이 null, empty, blank면 400 반환한다.")
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @ParameterizedTest
        void createWithPrUrlNullAndEmpty(final String prUrl) throws Exception {
            final ReviewCreateRequest request = new ReviewCreateRequest("리뷰 요청합니다.", "본문", prUrl);

            requestAboutReview(post("/reviewers/1/reviews"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }

        @DisplayName("prUrl이 형식에 맞지 않으면 400 반환한다.")
        @ValueSource(strings = {
                "ttps://github.com/bboor/project/pull/1", "github.combboor/project/pull/1",
                "github.com/bboor/project/pull/", "github.com/bboor/project/1", "github.com/pull/1",
                "github.com/project/pull/1", "/bboor/project/pull/1", "github.com/bboor/project/pull/1a"
        })
        @ParameterizedTest
        void createWithInvalidPrUrl(final String prUrl) throws Exception {
            final ReviewCreateRequest request = new ReviewCreateRequest("리뷰 요청합니다.", "본문", prUrl);

            requestAboutReview(post("/reviewers/1/reviews"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }
    }

    @DisplayName("단일 리뷰 상세 정보 조회 시")
    @Nested
    class ReadSingleReviewTest {

        @DisplayName("요청이 유효하면 200 반환한다.")
        @Test
        void validReadSingleReview() throws Exception {
            requestAboutReview(get("/reviewers/1/reviews/1"), null)
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("역할별 리뷰 목록 조회 시")
    @Nested
    class ReadReviewsByRoleTest {

        @DisplayName("요청이 유효하면 200 반환한다.")
        @ValueSource(strings = {"reviewee", "reviewer"})
        @ParameterizedTest
        void validReadReviewsByRole(final String role) throws Exception {
            requestAboutReview(get("/reviews?role=" + role), null)
                    .andExpect(status().isOk());
        }
    }

    @DisplayName("리뷰 수정 시")
    @Nested
    class ReviewUpdateTest {

        @DisplayName("요청이 유효하면 204 반환한다.")
        @Test
        void validUpdateReview() throws Exception {
            final ReviewUpdateRequest request = new ReviewUpdateRequest("본문");

            requestAboutReview(patch("/reviewers/1/reviews/1"), request)
                    .andExpect(status().isNoContent());
        }

        @DisplayName("content가 null, empty, blank면 400 반환한다.")
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @ParameterizedTest
        void updateWithContentNullAndEmpty(final String content) throws Exception {
            final ReviewUpdateRequest request = new ReviewUpdateRequest(content);

            requestAboutReview(patch("/reviewers/1/reviews/1"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }

        @DisplayName("content가 제한 길이 1500자보다 길면 400 반환한다.")
        @Test
        void updateWithContentGreaterThanMaxLen() throws Exception {
            final String content = makeStringByLength(1501);
            final ReviewUpdateRequest request = new ReviewUpdateRequest(content);

            requestAboutReview(patch("/reviewers/1/reviews/1"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }
    }

    @DisplayName("리뷰 승인 시")
    @Nested
    class ReviewAcceptTest {

        @DisplayName("요청이 유효하면 204 반환한다.")
        @Test
        void validAcceptReview() throws Exception {
            requestAboutReview(patch("/reviewers/1/reviews/1/status-accepted"), null)
                    .andExpect(status().isNoContent());
        }
    }

    @DisplayName("리뷰 거절 시")
    @Nested
    class ReviewRefuseTest {

        @DisplayName("요청이 유효하면 204 반환한다.")
        @Test
        void validAcceptReview() throws Exception {
            requestAboutReview(delete("/reviewers/1/reviews/1"), null)
                    .andExpect(status().isNoContent());
        }
    }

    private ResultActions requestAboutReview(
            final MockHttpServletRequestBuilder mockHttpServletRequestBuilder, final Object request
    ) throws Exception {
        final String accessToken = tokenProvider.createAccessToken(1L);
        return mockMvc.perform(mockHttpServletRequestBuilder
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private String makeStringByLength(final int length) {
        return "A".repeat(length);
    }
}

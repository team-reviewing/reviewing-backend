package project.reviewing.unit.review.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.MethodArgumentNotValidException;
import project.reviewing.review.presentation.request.ReviewCreateRequest;
import project.reviewing.unit.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ReviewController 는 ")
public class ReviewControllerTest extends ControllerTest {

    @DisplayName("Review 생성 시 ")
    @Nested
    class ReviewCreateTest {

        @DisplayName("요청이 유효하면 200 반환한다.")
        @Test
        void createReview() throws Exception {
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    1L, "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            requestToCreateReview(reviewCreateRequest)
                    .andExpect(status().isOk());
        }

        @DisplayName("reviewerMemberId가 1보다 작으면 400 반환한다.")
        @Test
        void invalidReviewerNameWithNullAndEmpty() throws Exception {
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    0L, "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            requestToCreateReview(reviewCreateRequest)
                    .andExpectAll(
                            result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException),
                            status().isBadRequest()
                    );
        }

        @DisplayName("title이 null이나 empty면 400 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void invalidTitleWithNullAndEmpty(final String title) throws Exception {
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    1L, title, "본문", "https://github.com/Tom/myproject/pull/1"
            );

            requestToCreateReview(reviewCreateRequest)
                    .andExpectAll(
                            result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException),
                            status().isBadRequest()
                    );
        }

        @DisplayName("title이 제한 길이 50자보다 길면 400 반환한다.")
        @Test
        void invalidTitleGreaterThanMaxLen() throws Exception {
            final String title = makeStringByLength(51);
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    1L, title, "본문", "https://github.com/Tom/myproject/pull/1"
            );

            requestToCreateReview(reviewCreateRequest)
                    .andExpectAll(
                            result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException),
                            status().isBadRequest()
                    );
        }

        @DisplayName("text가 null이나 empty면 400 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void invalidTextWithNullAndEmpty(final String text) throws Exception {
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    1L, "리뷰 요청합니다.", text, "https://github.com/Tom/myproject/pull/1"
            );

            requestToCreateReview(reviewCreateRequest)
                    .andExpectAll(
                            result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException),
                            status().isBadRequest()
                    );
        }

        @DisplayName("text가 제한 길이 500자보다 길면 400 반환한다.")
        @Test
        void invalidTextGreaterThanMaxLen() throws Exception {
            final String text = makeStringByLength(501);
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    1L, "리뷰 요청합니다.", text, "https://github.com/Tom/myproject/pull/1"
            );

            requestToCreateReview(reviewCreateRequest)
                    .andExpectAll(
                            result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException),
                            status().isBadRequest()
                    );
        }

        @DisplayName("prUrl이 null이나 empty면 400 반환한다.")
        @NullAndEmptySource
        @ParameterizedTest
        void invalidPrUrlWithNullAndEmpty(final String prUrl) throws Exception {
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    1L, "리뷰 요청합니다.", "본문", prUrl
            );

            requestToCreateReview(reviewCreateRequest)
                    .andExpectAll(
                            result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException),
                            status().isBadRequest()
                    );
        }
    }

    private ResultActions requestToCreateReview(final ReviewCreateRequest request) throws Exception {
        final String accessToken = tokenProvider.createAccessToken(1L);
        return mockMvc.perform(post("/reviews")
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

package project.reviewing.unit.review.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import project.reviewing.review.presentation.request.ReviewCreateRequest;
import project.reviewing.review.presentation.request.ReviewUpdateRequest;
import project.reviewing.unit.ControllerTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            mockMvc.perform(post("/reviewers/1/reviews")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createAccessToken(1L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(reviewCreateRequest)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @DisplayName("title이 null, empty, blank면 400 반환한다.")
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @ParameterizedTest
        void createReviewWithTitleNullAndEmpty(final String title) throws Exception {
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    title, "본문", "https://github.com/Tom/myproject/pull/1"
            );

            mockMvc.perform(post("/reviewers/1/reviews")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createAccessToken(1L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(reviewCreateRequest)))
                    .andDo(print())
                    .andExpectAll(
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class),
                            status().isBadRequest()
                    );
        }

        @DisplayName("title이 제한 길이 50자보다 길면 400 반환한다.")
        @Test
        void createReviewWithTitleGreaterThanMaxLen() throws Exception {
            final String title = makeStringByLength(51);
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    title, "본문", "https://github.com/Tom/myproject/pull/1"
            );

            mockMvc.perform(post("/reviewers/1/reviews")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createAccessToken(1L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(reviewCreateRequest)))
                    .andDo(print())
                    .andExpectAll(
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class),
                            status().isBadRequest()
                    );
        }

        @DisplayName("content가 null, empty, blank면 400 반환한다.")
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @ParameterizedTest
        void createReviewWithContentNullAndEmpty(final String content) throws Exception {
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    "리뷰 요청합니다.", content, "https://github.com/Tom/myproject/pull/1"
            );

            mockMvc.perform(post("/reviewers/1/reviews")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createAccessToken(1L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(reviewCreateRequest)))
                    .andDo(print())
                    .andExpectAll(
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class),
                            status().isBadRequest()
                    );
        }

        @DisplayName("content가 제한 길이 1500자보다 길면 400 반환한다.")
        @Test
        void createReviewWithContentGreaterThanMaxLen() throws Exception {
            final String content = makeStringByLength(1501);
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    "리뷰 요청합니다.", content, "https://github.com/Tom/myproject/pull/1"
            );

            mockMvc.perform(post("/reviewers/1/reviews")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createAccessToken(1L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(reviewCreateRequest)))
                    .andDo(print())
                    .andExpectAll(
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class),
                            status().isBadRequest()
                    );
        }

        @DisplayName("prUrl이 null, empty, blank면 400 반환한다.")
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @ParameterizedTest
        void createReviewWithPrUrlNullAndEmpty(final String prUrl) throws Exception {
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("리뷰 요청합니다.", "본문", prUrl);

            mockMvc.perform(post("/reviewers/1/reviews")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createAccessToken(1L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(reviewCreateRequest)))
                    .andDo(print())
                    .andExpectAll(
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class),
                            status().isBadRequest()
                    );
        }

        @DisplayName("prUrl이 형식에 맞지 않으면 400 반환한다.")
        @ValueSource(strings = {
                "ttps://github.com/bboor/project/pull/1", "github.combboor/project/pull/1",
                "github.com/bboor/project/pull/", "github.com/bboor/project/1", "github.com/pull/1",
                "github.com/project/pull/1", "/bboor/project/pull/1", "github.com/bboor/project/pull/1a"
        })
        @ParameterizedTest
        void createReviewWithInvalidPrUrl(final String prUrl) throws Exception {
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest("리뷰 요청합니다.", "본문", prUrl);

            mockMvc.perform(post("/reviewers/1/reviews")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createAccessToken(1L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(reviewCreateRequest)))
                    .andDo(print())
                    .andExpectAll(
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class),
                            status().isBadRequest()
                    );
        }
    }

    @DisplayName("리뷰 수정 시 ")
    @Nested
    class ReviewUpdateTest {

        @DisplayName("요청이 유효하면 204 반환한다.")
        @Test
        void validUpdateReview() throws Exception {
            final ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest("수정할 본문");

            mockMvc.perform(patch("/reviewers/1/reviews/1")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createAccessToken(1L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(reviewUpdateRequest)))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }

        @DisplayName("content가 null, empty, blank면 400 반환한다.")
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        @ParameterizedTest
        void updateReviewWithContentNullAndEmpty(final String updatingContent) throws Exception {
            final ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest(updatingContent);

            mockMvc.perform(patch("/reviewers/1/reviews/1")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createAccessToken(1L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(reviewUpdateRequest)))
                    .andDo(print())
                    .andExpectAll(
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class),
                            status().isBadRequest()
                    );
        }

        @DisplayName("content가 제한 길이 1500자보다 길면 400 반환한다.")
        @Test
        void updateReviewWithContentGreaterThanMaxLen() throws Exception {
            final String updatingContent = makeStringByLength(1501);
            final ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest(updatingContent);

            mockMvc.perform(patch("/reviewers/1/reviews/1")
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenProvider.createAccessToken(1L))
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8")
                            .content(objectMapper.writeValueAsString(reviewUpdateRequest)))
                    .andDo(print())
                    .andExpectAll(
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class),
                            status().isBadRequest()
                    );
        }
    }

    private String makeStringByLength(final int length) {
        return "A".repeat(length);
    }
}

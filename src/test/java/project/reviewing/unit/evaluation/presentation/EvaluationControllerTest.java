package project.reviewing.unit.evaluation.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.web.bind.MethodArgumentNotValidException;
import project.reviewing.evaluation.presentation.request.EvaluationCreateRequest;
import project.reviewing.unit.ControllerTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("EvaluationController는 ")
public class EvaluationControllerTest extends ControllerTest {

    @DisplayName("리뷰 평가 생성 시 ")
    @Nested
    class EvaluationCreateTest {

        @DisplayName("요청이 유효하면 200 반환한다.")
        @Test
        void validCreateEvaluation() throws Exception {
            final EvaluationCreateRequest request = new EvaluationCreateRequest(1L, 3.5F, "평가 내용");

            requestHttp(post("/reviewers/1/evaluations"), request)
                    .andExpect(status().isOk());
        }

        @DisplayName("reviewId가 null이면 400 반환한다.")
        @Test
        void createWithReviewIdNull() throws Exception {
            final EvaluationCreateRequest request = new EvaluationCreateRequest(null, 3.5F, "평가 내용");

            requestHttp(post("/reviewers/1/evaluations"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }

        @DisplayName("score가 null이거나 제한 범위를 벗어났다면 400 반환한다.")
        @ValueSource(floats = {-0.1F, 5.1F})
        @NullSource
        @ParameterizedTest
        void createWithTitleGreaterThanMaxLen(final Float score) throws Exception {
            final EvaluationCreateRequest request = new EvaluationCreateRequest(1L, score, "평가 내용");

            requestHttp(post("/reviewers/1/evaluations"), request)
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
            final EvaluationCreateRequest request = new EvaluationCreateRequest(null, 3.5F, content);

            requestHttp(post("/reviewers/1/evaluations"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }

        @DisplayName("content가 제한 길이 100자보다 길면 400 반환한다.")
        @Test
        void createWithContentGreaterThanMaxLen() throws Exception {
            final String content = makeStringByLength(101);
            final EvaluationCreateRequest request = new EvaluationCreateRequest(null, 3.5F, content);

            requestHttp(post("/reviewers/1/evaluations"), request)
                    .andExpectAll(
                            status().isBadRequest(),
                            result -> assertThat(result.getResolvedException())
                                    .isInstanceOf(MethodArgumentNotValidException.class)
                    );
        }
    }

    @DisplayName("단일 리뷰 평가 조회 시 ")
    @Nested
    class SingleEvaluationReadTest {

        @DisplayName("요청이 유효하면 200 반환한다.")
        @Test
        void validReadSingleEvaluation() throws Exception {
            requestHttp(get("/evaluations/1"), null)
                    .andExpect(status().isOk());
        }
    }
}

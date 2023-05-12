package project.reviewing.unit.member.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import project.reviewing.unit.ControllerTest;

@DisplayName("ReviewerController 는")
public class ReviewerControllerTest extends ControllerTest {

    @DisplayName("리뷰어 목록 조회 시")
    @Nested
    class ReviewersFindTest {

        @DisplayName("정상적인 경우 200을 반환한다.")
        @Test
        void findReviewers() throws Exception {
            mockMvc.perform(get("/reviewers"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @DisplayName("page 파라미터에 올바르지 않은 값이 입력되는 경우 400을 반환한다.")
        @ValueSource(strings = "-1")
        @EmptySource
        @ParameterizedTest
        void findReviewerByInvalidPage(final String page) throws Exception {
            mockMvc.perform(get("/reviewers")
                            .param("page", page))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @DisplayName("size 파라미터에 올바르지 않은 값이 입력되는 경우 400을 반환한다.")
        @ValueSource(strings = {"-1", "0"})
        @EmptySource
        @ParameterizedTest
        void findReviewerByInvalidSize(final String size) throws Exception {
            mockMvc.perform(get("/reviewers")
                            .param("size", size))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @DisplayName("단일 리뷰 조회 시 ")
    @Nested
    class SingleReviewerFindTest {

        @DisplayName("정상적인 경우 200 반환한다.")
        @Test
        void findSingleReviewer() throws Exception {
            mockMvc.perform(get("/reviewers/1"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}

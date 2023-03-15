package project.reviewing.unit.member.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.reviewing.unit.ControllerTest;

@DisplayName("ReviewerController 는")
public class ReviewerControllerTest extends ControllerTest {

    @DisplayName("리뷰어 목록 조회 시")
    @Nested
    class ReviewerFindTest {

        @DisplayName("정상적인 경우 200을 반환한다.")
        @Test
        void findReviewers() throws Exception {
            mockMvc.perform(get("/reviewers"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}

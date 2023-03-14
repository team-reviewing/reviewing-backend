package project.reviewing.unit.tag.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.reviewing.unit.ControllerTest;

@DisplayName("TagController 는")
public class TagControllerTest extends ControllerTest {

    @DisplayName("카테고리별로 태그 목록 조회 시")
    @Nested
    class TagFindTest {

        @DisplayName("정상적인 경우 200을 반환한다.")
        @Test
        void findTest() throws Exception {
            mockMvc.perform(get("/tags"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}

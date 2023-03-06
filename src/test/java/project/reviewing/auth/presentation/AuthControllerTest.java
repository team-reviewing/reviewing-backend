package project.reviewing.auth.presentation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import project.reviewing.auth.application.response.LoginResponse;
import project.reviewing.common.ControllerTest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends ControllerTest {

    @DisplayName("authorization code의 공백 여부를 검증한다.")
    @Test
    void validationTest() throws Exception {
        // given
        final String authorizationCode = " ";
        final LoginResponse loginResponse = new LoginResponse(1L, "Access Token", "Refresh Token", true);

        given(authService.githubLogin(authorizationCode))
                .willReturn(loginResponse);

        // when then
        mockMvc.perform(post("/auth/login/github")
                        .content(authorizationCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @DisplayName("login을 하면 Access Token과 Refresh Token을 cookie 형태로 반환한다.")
    @Test
    void loginTest() throws Exception {
        // given
        final String authorizationCode = "code";
        final LoginResponse loginResponse = new LoginResponse(1L, "Access Token", "Refresh Token", true);

        given(authService.githubLogin(authorizationCode))
                .willReturn(loginResponse);

        // when then
        mockMvc.perform(post("/auth/login/github")
                        .content(authorizationCode)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(cookie().value("access_token", "Access Token"))
                .andExpect(cookie().httpOnly("access_token", true))
                .andExpect(cookie().value("refresh_token", "Refresh Token"))
                .andExpect(cookie().httpOnly("refresh_token", true))
                .andExpect(cookie().path("refresh_token", "/auth/refresh"))
                .andDo(print());
    }
}

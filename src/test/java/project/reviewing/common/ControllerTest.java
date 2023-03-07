package project.reviewing.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;
import project.reviewing.auth.application.AuthService;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.auth.presentation.AuthInterceptor;
import project.reviewing.auth.presentation.RefreshInterceptor;
import project.reviewing.member.application.MemberService;

@WebMvcTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = RestController.class))
@Import({
        AuthInterceptor.class, RefreshInterceptor.class, TokenProvider.class
})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MemberService memberService;

    @Autowired
    protected TokenProvider tokenProvider;

    @MockBean
    protected RefreshTokenRepository refreshTokenRepository;
}

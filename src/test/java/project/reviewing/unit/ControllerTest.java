package project.reviewing.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import project.reviewing.auth.presentation.AuthContext;
import project.reviewing.auth.presentation.AuthInterceptor;
import project.reviewing.auth.presentation.RefreshInterceptor;
import project.reviewing.member.command.application.MemberService;
import project.reviewing.member.query.application.MemberQueryService;
import project.reviewing.review.query.application.ReviewQueryService;
import project.reviewing.tag.query.application.TagQueryService;
import project.reviewing.review.command.application.ReviewService;

@WebMvcTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = RestController.class))
@Import({
        TokenProvider.class, AuthContext.class
})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected TokenProvider tokenProvider;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected MemberQueryService memberQueryService;

    @MockBean
    protected ReviewService reviewService;

    @MockBean
    protected ReviewQueryService reviewQueryService;

    @MockBean
    protected RefreshTokenRepository refreshTokenRepository;

    @MockBean
    protected TagQueryService tagQueryService;
}

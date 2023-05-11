package project.reviewing.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.RestController;
import project.reviewing.auth.application.AuthService;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.auth.presentation.AuthContext;
import project.reviewing.member.command.application.MemberService;
import project.reviewing.member.query.application.MemberQueryService;
import project.reviewing.review.query.application.ReviewQueryService;
import project.reviewing.tag.query.application.TagQueryService;
import project.reviewing.review.command.application.ReviewService;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

    protected ResultActions requestHttp(
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

    protected String makeStringByLength(final int length) {
        return "A".repeat(length);
    }
}

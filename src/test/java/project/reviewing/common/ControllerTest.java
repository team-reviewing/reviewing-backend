package project.reviewing.common;

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
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.auth.presentation.AuthInterceptor;

@WebMvcTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = RestController.class))
@Import({
        TokenProvider.class, AuthInterceptor.class
})
public class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private TokenProvider tokenProvider;

    @MockBean
    protected AuthService authService;
}

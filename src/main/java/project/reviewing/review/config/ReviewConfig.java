package project.reviewing.review.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.reviewing.review.presentation.ReviewsByRoleParamInterceptor;

@RequiredArgsConstructor
@Configuration
public class ReviewConfig implements WebMvcConfigurer {

    private final ReviewsByRoleParamInterceptor reviewRoleInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(reviewRoleInterceptor)
                .addPathPatterns("/reviews");
    }
}

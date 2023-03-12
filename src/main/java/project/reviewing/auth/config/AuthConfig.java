package project.reviewing.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.reviewing.auth.presentation.AuthArgumentResolver;
import project.reviewing.auth.presentation.AuthContext;
import project.reviewing.auth.presentation.AuthInterceptor;
import project.reviewing.auth.presentation.RefreshInterceptor;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class AuthConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final RefreshInterceptor refreshInterceptor;
    private final AuthArgumentResolver authArgumentResolver;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/auth/login/*", "/auth/refresh");
        registry.addInterceptor(refreshInterceptor)
                .addPathPatterns("/auth/refresh");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }
}

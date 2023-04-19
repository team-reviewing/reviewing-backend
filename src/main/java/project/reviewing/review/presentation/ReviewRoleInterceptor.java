package project.reviewing.review.presentation;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ReviewRoleInterceptor implements HandlerInterceptor {

    private static final String ROLE_REVIEWER = "reviewer";
    private static final String ROLE_REVIEWEE = "reviewee";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String role = request.getParameter("role");

        if (!role.equals(ROLE_REVIEWER) && role.equals(ROLE_REVIEWEE)) {
            throw new BadRequestException(ErrorType.QUERY_PARAM_INVALID_FORMAT);
        }
        return true;
    }
}

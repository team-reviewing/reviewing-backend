package project.reviewing.review.presentation;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.review.presentation.data.RoleInReview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ReviewRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String role = request.getParameter("role");

        if (role == null || !RoleInReview.isContain(role)) {
            throw new BadRequestException(ErrorType.QUERY_PARAM_INVALID_FORMAT);
        }
        return true;
    }
}

package project.reviewing.review.presentation;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.review.command.domain.ReviewStatus;
import project.reviewing.review.presentation.data.RoleInReview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ReviewsByRoleParamInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String role = request.getParameter("role");
        final String status = request.getParameter("status");

        if (role != null && !RoleInReview.isContain(role)) {
            throw new BadRequestException(ErrorType.INVALID_FORMAT);
        }
        if (status != null && !ReviewStatus.isContain(status)) {
            throw new BadRequestException(ErrorType.INVALID_FORMAT);
        }
        return true;
    }
}

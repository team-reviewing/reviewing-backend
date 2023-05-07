package project.reviewing.common.presentation;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class PageableVerificationArgumentResolver extends PageableHandlerMethodArgumentResolver {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 1;

    @Override
    public Pageable resolveArgument(
            final MethodParameter methodParameter, final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory
    ) {
        final String page = webRequest.getParameter("page");
        final String size = webRequest.getParameter("size");

        if (isInvalidPageAndSize(page, size)) {
            throw new BadRequestException(ErrorType.INVALID_FORMAT);
        }

        return super.resolveArgument(methodParameter, mavContainer, webRequest, binderFactory);
    }

    private boolean isInvalidPageAndSize(final String page, final String size) {
        if (page == null && size == null) {
            return false;
        }
        if (page == null) {
            return isInvalidSize(size);
        }
        if (size == null) {
            return isInvalidPage(page);
        }
        return isInvalidPage(page) || isInvalidSize(size);
    }

    private boolean isInvalidPage(final String page) {
        return !isNumeric(page) || (Integer.parseInt(page) < DEFAULT_PAGE);
    }

    private boolean isInvalidSize(final String size) {
        return !isNumeric(size) || (Integer.parseInt(size) < DEFAULT_SIZE);
    }

    private boolean isNumeric(final String value) {
        if (value.isBlank()) {
            return false;
        }

        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

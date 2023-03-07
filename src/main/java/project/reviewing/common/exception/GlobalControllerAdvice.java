package project.reviewing.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.reviewing.auth.exception.InvalidTokenException;
import project.reviewing.auth.exception.RefreshTokenException;
import project.reviewing.common.response.ErrorResponse;
import project.reviewing.common.util.CookieBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidTokenException.class)
    public ErrorResponse handlerInvalidTokenException(final InvalidTokenException e, final HttpServletResponse response) {
        response.addHeader("WWW-Authenticated", "Basic realm=\"/auth/refresh\"");
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(RefreshTokenException.class)
    public ErrorResponse handlerRefreshTokenException(final RefreshTokenException e, final HttpServletResponse response) {
        response.addCookie(CookieBuilder.builder("refresh_token", "token_reset").maxAge(0).build());
        response.addHeader("WWW-Authenticated", "Basic realm=\"/auth/login/github\"");
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            BadRequestException.class,
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            ConstraintViolationException.class
    })
    public ErrorResponse handlerValidationBadRequestException(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleRuntimeException(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}

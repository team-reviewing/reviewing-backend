package project.reviewing.auth.exception;

import project.reviewing.common.exception.ErrorType;
import project.reviewing.common.exception.UnauthorizedException;

public class InvalidAuthenticationException extends UnauthorizedException {

    public InvalidAuthenticationException(final ErrorType errorType) {
        super(errorType);
    }
}

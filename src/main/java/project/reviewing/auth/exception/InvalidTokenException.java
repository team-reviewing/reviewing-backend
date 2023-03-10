package project.reviewing.auth.exception;

import project.reviewing.common.exception.ErrorType;
import project.reviewing.common.exception.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {

    public InvalidTokenException(final ErrorType errorType) {
        super(errorType);
    }
}

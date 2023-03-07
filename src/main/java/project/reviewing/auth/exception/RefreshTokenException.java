package project.reviewing.auth.exception;

import project.reviewing.common.exception.ErrorType;
import project.reviewing.common.exception.UnauthorizedException;

public class RefreshTokenException extends UnauthorizedException {

    public RefreshTokenException(final ErrorType errorType) {
        super(errorType);
    }
}

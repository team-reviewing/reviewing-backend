package project.reviewing.common.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

    private final ErrorType errorType;

    public UnauthorizedException(final ErrorType errorType) {
        this.errorType = errorType;
    }
}

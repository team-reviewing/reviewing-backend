package project.reviewing.common.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {

    private final String code;

    public UnauthorizedException(final ErrorType errorType) {
        super(errorType.getMessage());
        this.code = errorType.getCode();
    }
}

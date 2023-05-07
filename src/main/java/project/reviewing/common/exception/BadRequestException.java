package project.reviewing.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ErrorType errorType;

    public BadRequestException(final ErrorType errorType) {
        this.errorType = errorType;
    }
}

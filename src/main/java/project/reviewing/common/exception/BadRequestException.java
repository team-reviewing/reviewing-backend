package project.reviewing.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final String code;

    public BadRequestException(final ErrorType errorType) {
        super(errorType.getMessage());
        this.code = errorType.getCode();
    }
}

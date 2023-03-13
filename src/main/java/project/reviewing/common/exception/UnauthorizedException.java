package project.reviewing.common.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(final ErrorType errorType) {
        super(errorType.getMessage());
    }
}

package project.reviewing;

public class BadRequestException extends RuntimeException {

    public BadRequestException(final ErrorType errorType) {
        super(errorType.getMessage());
    }
}

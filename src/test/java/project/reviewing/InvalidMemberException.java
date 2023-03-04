package project.reviewing;

public class InvalidMemberException extends BadRequestException {

    public InvalidMemberException(final ErrorType errorType) {
        super(errorType);
    }
}

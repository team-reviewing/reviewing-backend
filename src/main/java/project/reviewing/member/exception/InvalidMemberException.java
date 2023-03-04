package project.reviewing.member.exception;

import project.reviewing.common.exception.ErrorType;
import project.reviewing.common.exception.BadRequestException;

public class InvalidMemberException extends BadRequestException {

    public InvalidMemberException(final ErrorType errorType) {
        super(errorType);
    }
}

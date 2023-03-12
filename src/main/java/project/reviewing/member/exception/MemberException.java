package project.reviewing.member.exception;

import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class MemberException extends BadRequestException {

    public MemberException(final ErrorType errorType) {
        super(errorType);
    }
}

package project.reviewing.member.exception;

import project.reviewing.common.exception.ErrorType;

public class MemberException extends RuntimeException {

    public MemberException(final ErrorType errorType) {
        super(errorType.getMessage());
    }
}

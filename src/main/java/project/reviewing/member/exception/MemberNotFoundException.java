package project.reviewing.member.exception;

import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class MemberNotFoundException extends BadRequestException {

    public MemberNotFoundException() {
        super(ErrorType.MEMBER_NOT_FOUND);
    }
}

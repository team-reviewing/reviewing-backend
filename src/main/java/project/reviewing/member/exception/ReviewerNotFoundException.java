package project.reviewing.member.exception;

import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class ReviewerNotFoundException extends BadRequestException {

    public ReviewerNotFoundException() {
        super(ErrorType.REVIEWER_NOT_FOUND);
    }
}

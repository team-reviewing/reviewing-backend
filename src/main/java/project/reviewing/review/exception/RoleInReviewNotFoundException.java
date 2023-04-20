package project.reviewing.review.exception;

import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class RoleInReviewNotFoundException extends BadRequestException {

    public RoleInReviewNotFoundException() {
        super(ErrorType.ROLE_IN_REVIEW_NOT_FOUND);
    }
}

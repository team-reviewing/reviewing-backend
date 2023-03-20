package project.reviewing.review.exception;

import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class ReviewNotFoundException extends BadRequestException {

    public ReviewNotFoundException() {
        super(ErrorType.REVIEW_NOT_FOUND);
    }
}

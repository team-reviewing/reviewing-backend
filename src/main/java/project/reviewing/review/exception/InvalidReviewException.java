package project.reviewing.review.exception;

import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class InvalidReviewException extends BadRequestException {

    public InvalidReviewException(final ErrorType errorType) {
        super(errorType);
    }
}

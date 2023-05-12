package project.reviewing.evaluation.exception;

import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class InvalidEvaluationException extends BadRequestException {

    public InvalidEvaluationException(final ErrorType errorType) {
        super(errorType);
    }
}

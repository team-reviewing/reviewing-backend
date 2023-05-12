package project.reviewing.evaluation.exception;

import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class EvaluationNotFoundException extends BadRequestException {

    public EvaluationNotFoundException() {
        super(ErrorType.EVALUATION_NOT_FOUND);
    }
}

package project.reviewing.member.exception;

import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class JobNotFoundException extends BadRequestException {

    public JobNotFoundException() {
        super(ErrorType.JOB_NOT_FOUND);
    }
}

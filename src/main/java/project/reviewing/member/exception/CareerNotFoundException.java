package project.reviewing.member.exception;

import project.reviewing.common.exception.BadRequestException;
import project.reviewing.common.exception.ErrorType;

public class CareerNotFoundException extends BadRequestException {

    public CareerNotFoundException() {
        super(ErrorType.CAREER_NOT_FOUND);
    }
}

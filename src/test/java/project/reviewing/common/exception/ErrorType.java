package project.reviewing.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    SAME_USERNAME_AS_BEFORE("기존과 동일한 사용자 이름입니다. 다시 입력해 주세요."),
    SAME_EMAIL_AS_BEFORE("기존과 동일한 이메일입니다. 다시 입력해 주세요."),
    ;

    private final String message;
}

package project.reviewing.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    NOT_AUTHENTICATED("인증되지 않은 요청입니다."),
    ALREADY_AUTHENTICATED("이미 인증 되었습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),

    API_FAILED("API 요청에 실패했습니다."),
    ;

    private final String message;
}

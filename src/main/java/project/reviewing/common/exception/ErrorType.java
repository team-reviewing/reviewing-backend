package project.reviewing.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    NOT_FOUND_MEMBER("유저 정보가 없습니다."),

    API_FAILED("API 요청에 실패했습니다."),
    ;

    private final String message;
}

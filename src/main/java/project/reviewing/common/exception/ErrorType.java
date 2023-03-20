package project.reviewing.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    MEMBER_NOT_FOUND("존재하지 않는 회원입니다."),
    REVIEWER_NOT_FOUND("해당 회원의 리뷰어 정보를 찾을 수 없습니다."),
    JOB_NOT_FOUND("해당 직무를 찾을 수 없습니다."),
    CAREER_NOT_FOUND("해당 경력을 찾을 수 없습니다."),
    SAME_USERNAME_AS_BEFORE("기존과 동일한 사용자 이름입니다. 다시 입력해 주세요."),
    SAME_EMAIL_AS_BEFORE("기존과 동일한 이메일입니다. 다시 입력해 주세요."),
    ALREADY_REGISTERED("이미 리뷰어 등록이 되었습니다."),
    DO_NOT_REGISTERED("리뷰어 등록을 하지 않았습니다. 먼저 리뷰어 등록을 해주세요."),
    ALREADY_REQUESTED("이미 해당 리뷰어에게 리뷰를 요청했습니다."),
    SAME_REVIEWER_AS_REVIEWEE("리뷰어와 리뷰이가 동일 회원입니다."),
    REVIEW_NOT_FOUND("해당 리뷰를 찾을 수 없습니다."),

    NOT_AUTHENTICATED("인증되지 않은 요청입니다."),
    ALREADY_AUTHENTICATED("이미 인증 되었습니다."),
    INVALID_TOKEN("유효하지 않은 토큰입니다."),

    PAGEABLE_INVALID_FORMAT("page 또는 size 형식이 유효하지 않습니다."),

    API_FAILED("API 요청에 실패했습니다."),
    ;

    private final String message;
}

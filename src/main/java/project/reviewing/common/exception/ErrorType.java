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
    ;

    private final String message;
}

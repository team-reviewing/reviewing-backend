package project.reviewing.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    ERROR("COMMON-001", "구체적으로 정의되지 않은 내부 오류입니다."),
    INVALID_FORMAT("COMMON-002", "요청 형식이 유효하지 않습니다."),
    API_FAILED("COMMON-003", "API 요청에 실패했습니다."),

    NOT_AUTHENTICATED("AUTH-001", "인증되지 않은 요청입니다."),
    ALREADY_AUTHENTICATED("AUTH-002", "이미 인증 되었습니다."),
    INVALID_TOKEN("AUTH-003", "유효하지 않은 토큰입니다."),

    MEMBER_NOT_FOUND("MEMBER-001", "존재하지 않는 회원입니다."),
    SAME_USERNAME_AS_BEFORE("MEMBER-002", "기존과 동일한 사용자 이름입니다. 다시 입력해 주세요."),
    SAME_EMAIL_AS_BEFORE("MEMBER-003", "기존과 동일한 이메일입니다. 다시 입력해 주세요."),
    JOB_NOT_FOUND("MEMBER-004", "해당 직무를 찾을 수 없습니다."),
    CAREER_NOT_FOUND("MEMBER-005", "해당 경력을 찾을 수 없습니다."),
    REVIEWER_NOT_FOUND("MEMBER-006", "해당 회원의 리뷰어 정보를 찾을 수 없습니다."),
    ALREADY_REGISTERED("MEMBER-007", "이미 리뷰어 등록이 되었습니다."),
    DO_NOT_REGISTERED("MEMBER-008", "리뷰어 등록을 하지 않았습니다. 먼저 리뷰어 등록을 해주세요."),

    REVIEW_NOT_FOUND("REVIEW-001", "해당 리뷰를 찾을 수 없습니다."),
    ALREADY_REQUESTED("REVIEW-002", "이미 해당 리뷰어에게 리뷰를 요청했습니다."),
    SAME_REVIEWER_AS_REVIEWEE("REVIEW-003", "리뷰어와 리뷰이가 동일 회원입니다."),
    NOT_REVIEWEE_OF_REVIEW("REVIEW-004", "해당 리뷰의 리뷰이가 아닙니다."),
    NOT_REVIEWER_OF_REVIEW("REVIEW-005", "해당 리뷰의 리뷰어가 아닙니다."),
    NOT_PROPER_REVIEW_STATUS("REVIEW-006", "리뷰의 상태가 적절하지 않습니다."),
    ROLE_IN_REVIEW_NOT_FOUND("REVIEW-007", "해당 리뷰의 역할을 찾을 수 없습니다."),

    ALREADY_EVALUATED("EVALUATION-001", "이미 해당 리뷰에 대한 평가가 되었습니다."),
    EVALUATION_NOT_FOUND("EVALUATION-002", "해당 평가를 찾을 수 없습니다.")
    ;

    private final String code;
    private final String message;
}

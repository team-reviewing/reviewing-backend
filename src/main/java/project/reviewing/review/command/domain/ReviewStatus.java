package project.reviewing.review.command.domain;

public enum ReviewStatus {

    CREATED("생성"),
    ACCEPTED("승인"),
    APPROVED("완료"),
    ;

    private final String value;

    ReviewStatus(final String value) {
        this.value = value;
    }
}

package project.reviewing.review.presentation.data;

import project.reviewing.review.exception.RoleInReviewNotFoundException;

import java.util.Arrays;

public enum RoleInReview {

    ROLE_REVIEWER("reviewer"),
    ROLE_REVIEWEE("reviewee"),
    ;

    private final String value;

    RoleInReview(final String value) {
        this.value = value;
    }

    public static boolean isContain(final String value) {
        return Arrays.stream(values())
                .anyMatch(role -> role.value.equals(value));
    }

    public static RoleInReview findValue(final String value) {
        return Arrays.stream(values())
                .filter(role -> role.value.equals(value))
                .findAny()
                .orElseThrow(RoleInReviewNotFoundException::new);
    }

    public boolean isReviewer() {
        return this.equals(ROLE_REVIEWER);
    }
}

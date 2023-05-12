package project.reviewing.review.command.domain;

import java.util.Arrays;

public enum ReviewStatus {

    NONE,
    CREATED,
    ACCEPTED,
    REFUSED,
    APPROVED,
    EVALUATED
    ;

    public static boolean isContain(final String name) {
        return Arrays.stream(values())
                .anyMatch(status -> status.name().equals(name));
    }

    public static ReviewStatus of(final String name) {
        return (name == null) ? ReviewStatus.NONE : ReviewStatus.valueOf(name);
    }

    public boolean isNone() {
        return this.equals(NONE);
    }
}

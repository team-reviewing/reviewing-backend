package project.reviewing.review.command.domain;

import java.util.Arrays;

public enum ReviewStatus {

    CREATED,
    ACCEPTED,
    APPROVED,
    ;

    public static boolean isContain(final String name) {
        return Arrays.stream(values())
                .anyMatch(status -> status.name().equals(name));
    }
}

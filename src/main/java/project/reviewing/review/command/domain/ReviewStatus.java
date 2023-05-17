package project.reviewing.review.command.domain;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ReviewStatus {

    NONE(0),
    CREATED(3),
    ACCEPTED(5),
    REFUSED(3),
    APPROVED(3),
    EVALUATED(3)
    ;

    private final int expirePeriod;

    ReviewStatus(int expirePeriod) {
        this.expirePeriod = expirePeriod;
    }

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

package project.reviewing.member.command.domain;

import java.util.Arrays;
import lombok.Getter;
import project.reviewing.member.exception.JobNotFoundException;

@Getter
public enum Job {

    BACKEND("백엔드"),
    FRONTEND("프론트엔드"),
    MOBILE("모바일"),
    ETC("기타")
    ;

    private final String value;

    Job(final String value) {
        this.value = value;
    }

    public static Job findValue(final String value) {
        return Arrays.stream(values())
                .filter(job -> job.value.equals(value))
                .findFirst()
                .orElseThrow(JobNotFoundException::new);
    }
}

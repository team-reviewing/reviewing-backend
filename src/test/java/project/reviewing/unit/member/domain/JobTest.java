package project.reviewing.unit.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.exception.JobNotFoundException;

@DisplayName("Job 은")
public class JobTest {

    @DisplayName("해당하는 값이 있는 경우 반환한다.")
    @ValueSource(strings = {"백엔드", "프론트엔드", "모바일"})
    @ParameterizedTest
    void findJob(final String value) {
        assertDoesNotThrow(() -> Job.findValue(value));
    }

    @DisplayName("해당하는 값이 없는 경우 예외를 반환한다.")
    @Test
    void notFoundJob() {
        assertThatThrownBy(() -> Job.findValue("신짱구"))
                .isInstanceOf(JobNotFoundException.class)
                .hasMessage(ErrorType.JOB_NOT_FOUND.getMessage());
    }
}

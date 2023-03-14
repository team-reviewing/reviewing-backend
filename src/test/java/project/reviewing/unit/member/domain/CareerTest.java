package project.reviewing.unit.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.exception.CareerNotFoundException;

@DisplayName("Career 는")
public class CareerTest {

    @DisplayName("해당하는 값이 있는 경우 반환한다.")
    @ValueSource(strings = {"신입", "주니어", "미들", "시니어"})
    @ParameterizedTest
    void findJob(final String value) {
        assertDoesNotThrow(() -> Career.findValue(value));
    }

    @DisplayName("해당하는 값이 없는 경우 예외를 반환한다.")
    @Test
    void notFoundJob() {
        assertThatThrownBy(() -> Career.findValue("신짱구"))
                .isInstanceOf(CareerNotFoundException.class)
                .hasMessage(ErrorType.CAREER_NOT_FOUND.getMessage());
    }

    @DisplayName("경력과 연차를 함께 보여줄 수 있다.")
    @CsvSource(value = {
            "신입, 신입(1년 이하)",
            "주니어, 주니어(1 ~ 3년)",
            "미들, 미들(4 ~ 8년)",
            "시니어, 시니어(9년 이상)"
    })
    @ParameterizedTest
    void getCareer(final String value, final String expected) {
        final Career sut = Career.findValue(value);

        final String actual = sut.getCareer();

        assertThat(actual).isEqualTo(expected);
    }
}

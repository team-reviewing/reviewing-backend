package project.reviewing.member.command.domain;

import java.util.Arrays;
import lombok.Getter;
import project.reviewing.member.exception.CareerNotFoundException;

@Getter
public enum Career {

    NEWCOMER("신입", "(1년 이하)"),
    JUNIOR("주니어", "(1 ~ 3년)"),
    MIDDLE("미들", "(4 ~ 8년)"),
    SENIOR("시니어", "(9년 이상)"),
    ;

    private final String value;
    private final String year;

    Career(final String value, final String year) {
        this.value = value;
        this.year = year;
    }

    public static Career findValue(final String value) {
        return Arrays.stream(values())
                .filter(career -> career.value.equals(value))
                .findFirst()
                .orElseThrow(CareerNotFoundException::new);
    }

    public static Career findByValueAndYear(final String valueAndYear) {
        return Arrays.stream(values())
                .filter(career -> career.getCareer().equals(valueAndYear))
                .findFirst()
                .orElseThrow(CareerNotFoundException::new);
    }

    public String getCareer() {
        return value + year;
    }
}

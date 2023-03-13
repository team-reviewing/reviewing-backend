package project.reviewing.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CookieType {

    REFRESH_TOKEN("refresh_token"),
    ;

    private final String value;
}

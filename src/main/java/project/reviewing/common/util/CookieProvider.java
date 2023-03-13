package project.reviewing.common.util;

import javax.servlet.http.Cookie;

public class CookieProvider {

    private static final String PATH_REFRESH_TOKEN = "/auth/refresh";

    public static Cookie createRefreshTokenCookie(final String refreshToken, final long validTime) {
        Cookie cookie = new Cookie(CookieType.REFRESH_TOKEN.getValue(), refreshToken);
        cookie.setMaxAge((int) validTime);
        cookie.setPath(PATH_REFRESH_TOKEN);
        cookie.setHttpOnly(true);
        return cookie;
    }
}

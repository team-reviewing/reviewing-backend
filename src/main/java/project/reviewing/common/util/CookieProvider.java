package project.reviewing.common.util;

import javax.servlet.http.Cookie;

public class CookieProvider {

    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String PATH_REFRESH_TOKEN = "/";

    public static Cookie createRefreshTokenCookie(final String refreshToken, final long validTime) {
        final Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
        cookie.setMaxAge((int) validTime);
        cookie.setPath(PATH_REFRESH_TOKEN);
        cookie.setHttpOnly(false);
        return cookie;
    }
}

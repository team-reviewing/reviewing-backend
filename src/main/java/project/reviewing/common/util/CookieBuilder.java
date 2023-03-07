package project.reviewing.common.util;

import javax.servlet.http.Cookie;

public class CookieBuilder {

    private String name;
    private String value;
    private int maxAge = -1;
    private String path;
    private boolean secure;
    private boolean httpOnly;

    private CookieBuilder(final String name, final String value) {
        this.name = name;
        this.value =value;
    }

    public static CookieBuilder builder(final String name, final String value) {
        return new CookieBuilder(name, value);
    }

    public CookieBuilder maxAge(final int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public CookieBuilder path(final String path) {
        this.path = path;
        return this;
    }

    public CookieBuilder secure(final boolean secure) {
        this.secure = secure;
        return this;
    }

    public CookieBuilder httpOnly(final boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public Cookie build() {
        Cookie cookie =  new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath(path);
        cookie.setSecure(secure);
        cookie.setHttpOnly(httpOnly);
        return cookie;
    }

    public static Cookie makeRemovedCookie(final String name, final String value) {
        return CookieBuilder.builder(name, value)
                .maxAge(0)
                .build();
    }
}

package project.reviewing.auth.application.response;

import lombok.Getter;

@Getter
public class RefreshResponse {

    private final String accessToken;
    private final String refreshToken;

    public RefreshResponse(final String accessToken, final String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

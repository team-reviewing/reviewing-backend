package project.reviewing.auth.application.response;

import lombok.Getter;

@Getter
public class GithubLoginResponse {

    private final Long memberId;
    private final String accessToken;
    private final String refreshToken;

    public GithubLoginResponse(final Long memberId, final String accessToken, final String refreshToken) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

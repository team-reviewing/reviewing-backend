package project.reviewing.auth.presentation.response;

import lombok.Getter;

@Getter
public class AccessTokenResponse {

    private final String accessToken;

    public AccessTokenResponse(final String accessToken) {
        this.accessToken = accessToken;
    }
}

package project.reviewing.auth.infrastructure.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubAuthRequest {

    private final String clientId;
    private final String clientSecret;
    private final String code;

    public GithubAuthRequest(final String clientId, final String clientSecret, final String code) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
    }
}

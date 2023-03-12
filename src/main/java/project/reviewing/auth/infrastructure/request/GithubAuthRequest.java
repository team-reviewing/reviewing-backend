package project.reviewing.auth.infrastructure.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GithubAuthRequest {

    @JsonProperty(value = "client_id")
    private final String clientId;

    @JsonProperty(value = "client_secret")
    private final String clientSecret;

    private final String code;

    public GithubAuthRequest(final String clientId, final String clientSecret, final String code) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.code = code;
    }
}

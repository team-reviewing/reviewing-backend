package project.reviewing.auth.infrastructure.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GithubAuthResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;
}

package project.reviewing.auth.infrastructure.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubAuthResponse {

    @JsonProperty(value = "access_token")
    private String accessToken;
}

package project.reviewing.auth.presentation.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubLoginRequest {

    @NotBlank
    private String authorizationCode;

    public GithubLoginRequest(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
}

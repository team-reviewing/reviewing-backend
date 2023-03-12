package project.reviewing.auth.infrastructure.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GithubProfileResponse {

    private Long id;

    private String login;

    @JsonProperty(value = "avatar_url")
    private String avatarUrl;

    @JsonProperty(value = "html_url")
    private String htmlUrl;

    private String email;
}

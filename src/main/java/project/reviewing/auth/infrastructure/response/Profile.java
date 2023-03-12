package project.reviewing.auth.infrastructure.response;

import lombok.Getter;

@Getter
public class Profile {

    private final Long id;
    private final String username;
    private final String email;
    private final String imageURL;
    private final String githubURL;

    public Profile(
            final Long id, final String username, final String email,
            final String imageURL, final String githubURL
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.imageURL = imageURL;
        this.githubURL = githubURL;
    }
}

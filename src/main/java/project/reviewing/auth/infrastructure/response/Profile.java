package project.reviewing.auth.infrastructure.response;

import lombok.Getter;

@Getter
public class Profile {

    private final Long id;
    private final String username;
    private final String email;
    private final String imageUrl;
    private final String githubUrl;

    public Profile(
            final Long id, final String username, final String email,
            final String imageUrl, final String githubUrl
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.githubUrl = githubUrl;
    }
}

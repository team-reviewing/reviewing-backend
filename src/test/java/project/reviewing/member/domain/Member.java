package project.reviewing.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    private Long id;

    private Long githubId;

    private String username;

    private String email;

    private String imageUrl;

    private boolean isReviewer;

    public Member(final Long githubId, final String username, final String email, final String imageUrl) {
        this.githubId = githubId;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public void update(final Member member) {
        this.username = member.getUsername();
        this.email = member.getEmail();
    }
}

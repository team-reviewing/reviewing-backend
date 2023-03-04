package project.reviewing.member.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.reviewing.ErrorType;
import project.reviewing.InvalidMemberException;

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
        updateUsername(member.getUsername());
        this.email = member.getEmail();
    }

    private void updateUsername(final String username) {
        if (this.username.equals(username)) {
            throw new InvalidMemberException(ErrorType.SAME_USERNAME_AS_BEFORE);
        }
        this.username = username;
    }
}

package project.reviewing.member.command.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.exception.InvalidMemberException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long githubId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String imageUrl;

    private String profileUrl;

    private boolean isReviewer;

    public Member(final Long githubId, final String username, final String email, final String imageUrl) {
        this.githubId = githubId;
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.isReviewer = false;
    }

    public void update(final Member member) {
        updateUsername(member.getUsername());
        updateEmail(member.getEmail());
    }

    private void updateUsername(final String username) {
        if (this.username.equals(username)) {
            throw new InvalidMemberException(ErrorType.SAME_USERNAME_AS_BEFORE);
        }
        this.username = username;
    }

    private void updateEmail(final String email) {
        if (this.email.equals(email)) {
            throw new InvalidMemberException(ErrorType.SAME_EMAIL_AS_BEFORE);
        }
        this.email = email;
    }
}

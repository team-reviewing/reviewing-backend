package project.reviewing.member.command.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
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

    @Column(nullable = false)
    private String email;

    private String imageUrl;

    @Column(nullable = false, unique = true)
    private String profileUrl;

    private boolean isReviewer;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "member")
    private Reviewer reviewer;

    public Member(final Long githubId, final String username, final String email, final String imageUrl, final String profileUrl) {
        this.githubId = githubId;
        this.username = username;
        this.email = ((email == null) ? "" : email);
        this.imageUrl = imageUrl;
        this.profileUrl = profileUrl;
        this.isReviewer = false;
    }

    public void updateLoginInformation(final Member member) {
        this.profileUrl = member.getProfileUrl();
    }

    public void update(final Member member) {
        this.username = member.getUsername();
        this.email = member.getEmail();
    }

    public void register(final Reviewer reviewer) {
        if (this.reviewer != null) {
            throw new InvalidMemberException(ErrorType.ALREADY_REGISTERED);
        }
        this.reviewer = reviewer;
        this.isReviewer = true;
        if (reviewer.getMember() != this) {
            reviewer.addMember(this);
        }
    }

    public void updateReviewer(final Reviewer reviewer) {
        this.reviewer.update(reviewer);
    }

    public void changeReviewerStatus() {
        if (this.reviewer == null) {
            throw new InvalidMemberException(ErrorType.DO_NOT_REGISTERED);
        }
        this.isReviewer = !isReviewer;
    }

    public void updateReviewerScore(final Float evaluationScore) {
        reviewer.updateScore(evaluationScore);
    }

    public boolean isReviewerRegistered() {
        return this.reviewer != null;
    }
}

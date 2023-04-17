package project.reviewing.review.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.review.exception.InvalidReviewException;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long revieweeId;

    @Column(nullable = false)
    private Long reviewerId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String prUrl;

    public static Review assign(
            final Long revieweeId, final Long reviewerId, final String title,
            final String content, final String prUrl, final Long reviewerMemberId, final boolean isReviewer
    ) {
        if (revieweeId.equals(reviewerMemberId)) {
            throw new InvalidReviewException(ErrorType.SAME_REVIEWER_AS_REVIEWEE);
        }
        if (!isReviewer) {
            throw new InvalidReviewException(ErrorType.DO_NOT_REGISTERED);
        }

        return new Review(revieweeId, reviewerId, title, content, prUrl);
    }

    public void updateReview(final Long revieweeId, final String updatingContent) {
        if (!this.revieweeId.equals(revieweeId)) {
            throw new InvalidReviewException(ErrorType.NOT_REVIEWEE_OF_REVIEW);
        }
        this.content = updatingContent;
    }

    private Review(
            final Long revieweeId, final Long reviewerId, final String title, final String content, final String prUrl
    ) {
        this.revieweeId = revieweeId;
        this.reviewerId = reviewerId;
        this.title = title;
        this.content = content;
        this.prUrl = prUrl;
    }
}

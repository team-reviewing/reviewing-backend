package project.reviewing.review.command.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.common.util.Time;
import project.reviewing.review.exception.InvalidReviewException;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    @Column(nullable = false)
    private LocalDateTime statusSetAt;

    public static Review assign(
            final Long revieweeId, final Long reviewerId, final String title, final String content,
            final String prUrl, final Long reviewerMemberId, final boolean isReviewer, Time time
    ) {
        if (revieweeId.equals(reviewerMemberId)) {
            throw new InvalidReviewException(ErrorType.SAME_REVIEWER_AS_REVIEWEE);
        }
        if (!isReviewer) {
            throw new InvalidReviewException(ErrorType.DO_NOT_REGISTERED);
        }

        return new Review(revieweeId, reviewerId, title, content, prUrl, ReviewStatus.CREATED, time.now());
    }

    public void update(final Long revieweeId, final String updatingContent) {
        if (!this.revieweeId.equals(revieweeId)) {
            throw new InvalidReviewException(ErrorType.NOT_REVIEWEE_OF_REVIEW);
        }
        content = updatingContent;
    }

    public void accept(Time time) {
        status = ReviewStatus.ACCEPTED;
        statusSetAt = time.now();
    }

    public void refuse(Time time) {
        status = ReviewStatus.REFUSED;
        statusSetAt = time.now();
    }

    public void approve(Time time) {
        status = ReviewStatus.APPROVED;
        statusSetAt = time.now();
    }

    public boolean canAccept(final Long reviewerId) {
        checkReviewer(reviewerId);
        checkStatusCreated();
        return true;
    }

    public boolean canRefuse(final Long reviewerId) {
        checkReviewer(reviewerId);
        checkStatusCreated();
        return true;
    }

    public boolean canApprove(final Long reviewerId) {
        checkReviewer(reviewerId);
        checkStatusAccepted();
        return true;
    }

    public boolean canFinish(final Long reviewerId) {
        checkReviewer(reviewerId);
        checkStatusRefused();
        return true;
    }

    public boolean isExpiredInRefusedStatus() {
        return (status == ReviewStatus.REFUSED) && isExpiredInDays(3);
    }

    public boolean isExpiredInApprovedStatus() {
        return (status == ReviewStatus.APPROVED) && isExpiredInDays(3);
    }

    private void checkReviewer(final Long reviewerId) {
        if (!this.reviewerId.equals(reviewerId)) {
            throw new InvalidReviewException(ErrorType.NOT_REVIEWER_OF_REVIEW);
        }
    }

    private void checkStatusCreated() {
        if (status != ReviewStatus.CREATED) {
            throw new InvalidReviewException(ErrorType.NOT_PROPER_REVIEW_STATUS);
        }
    }

    private void checkStatusAccepted() {
        if (status != ReviewStatus.ACCEPTED) {
            throw new InvalidReviewException(ErrorType.NOT_PROPER_REVIEW_STATUS);
        }
    }

    private void checkStatusRefused() {
        if (status != ReviewStatus.REFUSED) {
            throw new InvalidReviewException(ErrorType.NOT_PROPER_REVIEW_STATUS);
        }
    }

    private boolean isExpiredInDays(final int days) {
        return statusSetAt.plusDays(3).isBefore(LocalDateTime.now())
                || statusSetAt.plusDays(3).isEqual(LocalDateTime.now());
    }

    private Review(
            final Long revieweeId, final Long reviewerId, final String title, final String content,
            final String prUrl, final ReviewStatus status, final LocalDateTime statusSetAt
    ) {
        this.revieweeId = revieweeId;
        this.reviewerId = reviewerId;
        this.title = title;
        this.content = content;
        this.prUrl = prUrl;
        this.status = status;
        this.statusSetAt = statusSetAt;;
    }
}

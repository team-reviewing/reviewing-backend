package project.reviewing.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.member.command.domain.ReviewerRepository;
import project.reviewing.member.exception.ReviewerNotFoundException;
import project.reviewing.review.domain.Review;
import project.reviewing.review.domain.ReviewRepository;
import project.reviewing.review.exception.InvalidReviewException;
import project.reviewing.review.exception.ReviewNotFoundException;
import project.reviewing.review.presentation.request.ReviewCreateRequest;
import project.reviewing.review.presentation.request.ReviewUpdateRequest;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewerRepository reviewerRepository;

    public void createReview(final Long revieweeId, final Long reviewerId, final ReviewCreateRequest reviewCreateRequest) {
        if (reviewRepository.existsByRevieweeIdAndReviewerId(revieweeId, reviewerId)) {
            throw new InvalidReviewException(ErrorType.ALREADY_REQUESTED);
        }

        final Reviewer reviewer = findReviewerById(reviewerId);
        final Review newReview = reviewCreateRequest.toEntity(
                revieweeId, reviewerId, reviewer.getMember().getId(), reviewer.getMember().isReviewer()
        );
        reviewRepository.save(newReview);
    }

    public void updateReview(final Long revieweeId, final Long reviewId, final ReviewUpdateRequest reviewUpdateRequest) {
        final Review review = findReviewById(reviewId);
        final Review updatedReview = reviewUpdateRequest.toEntity(revieweeId);

        review.update(updatedReview);
    }

    private Reviewer findReviewerById(final Long id) {
        return reviewerRepository.findById(id)
                .orElseThrow(ReviewerNotFoundException::new);
    }

    private Review findReviewById(final Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(ReviewNotFoundException::new);
    }
}

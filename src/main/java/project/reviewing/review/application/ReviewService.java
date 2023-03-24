package project.reviewing.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;
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
    private final MemberRepository memberRepository;

    public void createReview(final Long revieweeId, final Long reviewerId, final ReviewCreateRequest reviewCreateRequest) {
        if (reviewRepository.existsByRevieweeIdAndReviewerId(revieweeId, reviewerId)) {
            throw new InvalidReviewException(ErrorType.ALREADY_REQUESTED);
        }

        final Member reviewerMember = memberRepository.findByReviewerId(reviewerId)
                .orElseThrow(MemberNotFoundException::new);
        final Review newReview = reviewCreateRequest.toEntity(
                revieweeId, reviewerId, reviewerMember.getId(), reviewerMember.isReviewer()
        );
        reviewRepository.save(newReview);
    }

    public void updateReview(final Long revieweeId, final Long reviewId, final ReviewUpdateRequest reviewUpdateRequest) {
        final Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ReviewNotFoundException::new);

        review.update(revieweeId, reviewUpdateRequest.getContent());
    }
}

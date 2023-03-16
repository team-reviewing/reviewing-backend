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
import project.reviewing.review.presentation.request.ReviewCreateRequest;

@Transactional
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public void createReview(final Long revieweeId, final Long reviewerId, final ReviewCreateRequest reviewCreateRequest) {
        if (revieweeId.equals(reviewerId)) {
            throw new InvalidReviewException(ErrorType.SAME_REVIEWER_AS_REVIEWEE);
        }

        final Member reviewer = findMemberByReviewerId(reviewerId);

        if (!reviewer.isReviewer()) {
            throw new InvalidReviewException(ErrorType.DO_NOT_REGISTERED);
        }
        reviewRepository.findByRevieweeIdAndReviewerId(revieweeId, reviewerId)
                .ifPresent(review -> { throw new InvalidReviewException(ErrorType.ALREADY_REQUESTED); });

        reviewRepository.save(makeReview(revieweeId, reviewerId, reviewCreateRequest));
    }

    private Member findMemberByReviewerId(final Long reviewerId) {
        return memberRepository.findById(reviewerId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Review makeReview(
            final Long revieweeId, final Long reviewerId, final ReviewCreateRequest reviewCreateRequest
    ) {
        return new Review(
                revieweeId, reviewerId, reviewCreateRequest.getTitle(),
                reviewCreateRequest.getContent(), reviewCreateRequest.getPrUrl()
        );
    }
}

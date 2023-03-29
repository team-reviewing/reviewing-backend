package project.reviewing.review.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.review.application.response.ReviewReadResponse;
import project.reviewing.review.domain.Review;
import project.reviewing.review.domain.ReviewRepository;
import project.reviewing.review.exception.InvalidReviewException;
import project.reviewing.review.presentation.request.ReviewCreateRequest;

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

        final Member reviewer = findMemberByReviewerId(reviewerId);
        final Review newReview = Review.assign(
                revieweeId, reviewerId, reviewCreateRequest.getTitle(),
                reviewCreateRequest.getContent(), reviewCreateRequest.getPrUrl(), reviewer.isReviewer()
        );
        reviewRepository.save(newReview);
    }

    public ReviewReadResponse readReview(final Long reviewId) {
        return new ReviewReadResponse(1, 1, "", "", "");
    }

    private Member findMemberByReviewerId(final Long reviewerId) {
        return memberRepository.findById(reviewerId)
                .orElseThrow(MemberNotFoundException::new);
    }
}

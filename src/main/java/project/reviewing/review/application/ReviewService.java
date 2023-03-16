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

    public void createReview(final long revieweeId, final ReviewCreateRequest reviewCreateRequest) {
        if (revieweeId == reviewCreateRequest.getReviewerMemberId()) {
            throw new InvalidReviewException(ErrorType.SAME_REVIEWER_AS_REVIEWEE);
        }

        final Member reviewerMember = findMemberById(reviewCreateRequest.getReviewerMemberId());

        if (!reviewerMember.isReviewer()) {
            throw new InvalidReviewException(ErrorType.DO_NOT_REGISTERED);
        }
        reviewRepository.findByRevieweeIdAndReviewerId(revieweeId, reviewerMember.getId())
                .ifPresent(review -> { throw new InvalidReviewException(ErrorType.ALREADY_REQUESTED); });

        reviewRepository.save(makeReview(revieweeId, reviewerMember.getId(), reviewCreateRequest));
    }

    private Member findMemberById(final long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Review makeReview(
            final long revieweeId, final long reviewerId, final ReviewCreateRequest reviewCreateRequest
    ) {
        return new Review(
                revieweeId, reviewerId, reviewCreateRequest.getTitle(),
                reviewCreateRequest.getText(), reviewCreateRequest.getPrUrl()
        );
    }
}

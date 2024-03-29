package project.reviewing.review.command.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.common.util.Time;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.member.query.dao.MemberDao;
import project.reviewing.review.command.application.response.SingleReviewReadResponse;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewRepository;
import project.reviewing.review.exception.InvalidReviewException;
import project.reviewing.review.exception.ReviewNotFoundException;
import project.reviewing.review.presentation.request.ReviewCreateRequest;
import project.reviewing.review.presentation.request.ReviewUpdateRequest;
import project.reviewing.review.query.dao.ReviewDAO;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewDAO reviewDAO;
    private final MemberRepository memberRepository;
    private final MemberDao memberDao;

    private final Time time;

    public void createReview(final Long revieweeId, final Long reviewerId, final ReviewCreateRequest request) {
        if (reviewDAO.existsByRevieweeIdAndReviewerIdWithNotApprovedAndEvaluated(revieweeId, reviewerId)) {
            throw new InvalidReviewException(ErrorType.ALREADY_REQUESTED);
        }

        final Member reviewerMember = memberDao.findByReviewerIdBySlockOnReviewer(reviewerId)
                .orElseThrow(MemberNotFoundException::new);
        final Review newReview = request.toEntity(
                revieweeId, reviewerId, reviewerMember.getId(), reviewerMember.isReviewer(), time
        );
        reviewRepository.save(newReview);
    }

    @Transactional(readOnly = true)
    public SingleReviewReadResponse readSingleReview(final Long reviewId) {
        final Review review = findReviewById(reviewId);

        return SingleReviewReadResponse.from(review);
    }

    public void updateReview(final Long revieweeId, final Long reviewId, final ReviewUpdateRequest request) {
        final Review review = findReviewById(reviewId);

        review.update(revieweeId, request.getContent());
    }

    public void acceptReview(final Long memberId, final Long reviewId) {
        final Review review = findReviewById(reviewId);
        final Member reviewerMember = findMemberById(memberId);

        if (review.canAccept(reviewerMember.getReviewer().getId())) {
            review.accept(time);
        }
    }

    public void refuseReview(final Long memberId, final Long reviewId) {
        final Review review = findReviewById(reviewId);
        final Member reviewerMember = findMemberById(memberId);

        if (review.canRefuse(reviewerMember.getReviewer().getId())) {
            review.refuse(time);
        }
    }

    public void approveReview(final Long memberId, final Long reviewId) {
        final Review review = findReviewById(reviewId);
        final Member reviewerMember = findMemberById(memberId);

        if (review.canApprove(reviewerMember.getReviewer().getId())) {
            review.approve(time);
        }
    }

    public void closeReview(final Long memberId, final Long reviewId) {
        final Review review = findReviewById(reviewId);
        final Member reviewerMember = findMemberById(memberId);

        if (review.canClose(reviewerMember.getId(), reviewerMember.getReviewer().getId())) {
            reviewRepository.delete(review);
        }
    }

    private Review findReviewById(final Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(ReviewNotFoundException::new);
    }

    private Member findMemberById(final Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }
}

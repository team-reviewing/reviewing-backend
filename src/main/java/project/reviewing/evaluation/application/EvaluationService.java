package project.reviewing.evaluation.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.evaluation.domain.Evaluation;
import project.reviewing.evaluation.domain.EvaluationRepository;
import project.reviewing.evaluation.exception.InvalidEvaluationException;
import project.reviewing.evaluation.presentation.request.EvaluationCreateRequest;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewRepository;
import project.reviewing.review.exception.ReviewNotFoundException;

@Transactional
@RequiredArgsConstructor
@Service
public class EvaluationService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final EvaluationRepository evaluationRepository;

    public void createEvaluation(
            final Long memberId,
            final Long reviewerId,
            final EvaluationCreateRequest evaluationCreateRequest
    ) {
        final Member reviewerMember = memberRepository.findByReviewerId(reviewerId)
                .orElseThrow(MemberNotFoundException::new);
        final Review review = reviewRepository.findById(evaluationCreateRequest.getReviewId())
                .orElseThrow(ReviewNotFoundException::new);

        if (evaluationRepository.existsByReviewId(evaluationCreateRequest.getReviewId())) {
            throw new InvalidEvaluationException(ErrorType.ALREADY_EVALUATED);
        }
        if (!memberId.equals(review.getRevieweeId())) {
            throw new InvalidEvaluationException(ErrorType.NOT_REVIEWEE_OF_REVIEW);
        }
        if (!reviewerMember.getReviewer().getId().equals(review.getReviewerId())) {
            throw new InvalidEvaluationException(ErrorType.NOT_REVIEWER_OF_REVIEW);
        }

        Evaluation evaluation = evaluationCreateRequest.toEntity(reviewerId, memberId);
        evaluationRepository.save(evaluation);
    }
}

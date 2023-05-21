package project.reviewing.evaluation.query.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.evaluation.command.application.response.EvaluationsForReviewerResponse;
import project.reviewing.evaluation.query.application.response.SingleEvaluationResponse;
import project.reviewing.evaluation.command.domain.Evaluation;
import project.reviewing.evaluation.command.domain.EvaluationRepository;
import project.reviewing.evaluation.exception.EvaluationNotFoundException;
import project.reviewing.evaluation.query.dao.EvaluationsDAO;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EvaluationQueryService {

    private final EvaluationRepository evaluationRepository;
    private final EvaluationsDAO evaluationsDAO;
    private final MemberRepository memberRepository;

    public SingleEvaluationResponse findSingleEvaluationByReviewId(final Long reviewId) {
        final Evaluation evaluation = evaluationRepository.findByReviewId(reviewId)
                .orElseThrow(EvaluationNotFoundException::new);

        return SingleEvaluationResponse.from(evaluation);
    }

    public EvaluationsForReviewerResponse findEvaluationsForReviewerInPage(final Long reviewerId, final Pageable pageable) {
        return EvaluationsForReviewerResponse.of(evaluationsDAO.findEvaluationsByReviewerId(reviewerId, pageable));
    }

    public EvaluationsForReviewerResponse findMyEvaluationsInPage(final Long memberId, final Pageable pageable) {
        final Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);

        return EvaluationsForReviewerResponse.of(
                evaluationsDAO.findEvaluationsByReviewerId(member.getReviewer().getId(), pageable)
        );
    }
}

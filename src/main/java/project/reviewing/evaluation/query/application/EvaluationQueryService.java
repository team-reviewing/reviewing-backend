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

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EvaluationQueryService {

    private final EvaluationRepository evaluationRepository;
    private final EvaluationsDAO evaluationsDAO;

    public SingleEvaluationResponse findSingleEvaluationByReviewId(final Long reviewId) {
        final Evaluation evaluation = evaluationRepository.findByReviewId(reviewId)
                .orElseThrow(EvaluationNotFoundException::new);

        return SingleEvaluationResponse.from(evaluation);
    }

    public EvaluationsForReviewerResponse findEvaluationsForReviewerInPage(final Long reviewerId, final Pageable pageable) {
        return EvaluationsForReviewerResponse.of(evaluationsDAO.findEvaluationsByReviewerId(reviewerId, pageable));
    }
}

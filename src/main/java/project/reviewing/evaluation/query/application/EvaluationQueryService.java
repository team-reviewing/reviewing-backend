package project.reviewing.evaluation.query.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.evaluation.query.application.response.SingleEvaluationResponse;
import project.reviewing.evaluation.command.domain.Evaluation;
import project.reviewing.evaluation.command.domain.EvaluationRepository;
import project.reviewing.evaluation.exception.EvaluationNotFoundException;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class EvaluationQueryService {

    private final EvaluationRepository evaluationRepository;

    public SingleEvaluationResponse findSingleEvaluationByReviewId(final Long reviewId) {
        final Evaluation evaluation = evaluationRepository.findByReviewId(reviewId)
                .orElseThrow(EvaluationNotFoundException::new);

        return SingleEvaluationResponse.from(evaluation);
    }
}

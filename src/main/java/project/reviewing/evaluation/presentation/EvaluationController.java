package project.reviewing.evaluation.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.presentation.AuthenticatedMember;
import project.reviewing.evaluation.command.application.EvaluationService;
import project.reviewing.evaluation.command.application.response.EvaluationsForReviewerResponse;
import project.reviewing.evaluation.query.application.response.SingleEvaluationResponse;
import project.reviewing.evaluation.presentation.request.EvaluationCreateRequest;
import project.reviewing.evaluation.query.application.EvaluationQueryService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class EvaluationController {

    private final EvaluationService evaluationService;
    private final EvaluationQueryService evaluationQueryService;

    @PostMapping("/reviewers/{reviewer-id}/evaluations")
    public void createEvaluation(
            @AuthenticatedMember final Long memberId,
            @PathVariable("reviewer-id") final Long reviewerId,
            @Valid @RequestBody final EvaluationCreateRequest evaluationCreateRequest
    ) {
        evaluationService.createEvaluation(memberId, reviewerId, evaluationCreateRequest);
    }

    @GetMapping("/evaluations")
    public EvaluationsForReviewerResponse findEvaluationsForReviewer(
            @PageableDefault(size = 3) final Pageable pageable,
            @RequestParam(name = "reviewerId") final Long reviewerId
    ) {
        return evaluationQueryService.findEvaluationsForReviewerInPage(reviewerId, pageable);
    }

    @GetMapping("/evaluations/{review-id}")
    public SingleEvaluationResponse readSingleEvaluation(@PathVariable("review-id") final Long reviewId) {
        return evaluationQueryService.findSingleEvaluationByReviewId(reviewId);
    }
}

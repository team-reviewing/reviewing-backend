package project.reviewing.evaluation.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.reviewing.auth.presentation.AuthenticatedMember;
import project.reviewing.evaluation.application.EvaluationService;
import project.reviewing.evaluation.application.response.SingleEvaluationResponse;
import project.reviewing.evaluation.presentation.request.EvaluationCreateRequest;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping("/reviewers/{reviewer-id}/evaluations")
    public void createEvaluation(
            @AuthenticatedMember final Long memberId,
            @PathVariable("reviewer-id") final Long reviewerId,
            @Valid @RequestBody final EvaluationCreateRequest evaluationCreateRequest
    ) {
        evaluationService.createEvaluation(memberId, reviewerId, evaluationCreateRequest);
    }

    @GetMapping("/evaluations/{review-id}")
    public SingleEvaluationResponse readSingleEvaluation(
            @AuthenticatedMember final Long memberId,
            @PathVariable("review-id") final Long reviewId
    ) {
        return new SingleEvaluationResponse();
    }
}

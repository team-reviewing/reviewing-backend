package project.reviewing.evaluation.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.reviewing.auth.presentation.AuthenticatedMember;
import project.reviewing.evaluation.presentation.request.EvaluationCreateRequest;

@RequiredArgsConstructor
@RestController
public class EvaluationController {

    @PostMapping("/reviewers/{reviewer-id}/evaluations")
    public void createEvaluation(
            @AuthenticatedMember final Long memberId,
            @PathVariable("reviewer-id") final Long reviewerId,
            @RequestBody final EvaluationCreateRequest evaluationCreateRequest
    ) {

    }
}

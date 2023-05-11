package project.reviewing.evaluation.presentation.request;

import lombok.Getter;

@Getter
public class EvaluationCreateRequest {

    private final Long reviewId;
    private final Float score;
    private final String content;

    public EvaluationCreateRequest(final Long reviewId, final Float score, final String content) {
        this.reviewId = reviewId;
        this.score = score;
        this.content = content;
    }
}

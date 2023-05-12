package project.reviewing.evaluation.application.response;

import lombok.Getter;
import project.reviewing.evaluation.domain.Evaluation;

@Getter
public class SingleEvaluationResponse {

    private Long id;
    private Float score;
    private String content;

    public static SingleEvaluationResponse from(final Evaluation evaluation) {
        return new SingleEvaluationResponse(evaluation.getId(), evaluation.getScore(), evaluation.getContent());
    }

    public SingleEvaluationResponse() {}

    private SingleEvaluationResponse(final Long id, final Float score, final String content) {
        this.id = id;
        this.score = score;
        this.content = content;
    }
}

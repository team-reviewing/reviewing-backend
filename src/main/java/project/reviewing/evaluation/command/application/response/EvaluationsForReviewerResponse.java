package project.reviewing.evaluation.command.application.response;

import lombok.Getter;

import java.util.List;

@Getter
public class EvaluationsForReviewerResponse {

    private List<EvaluationSummary> evaluations;
    private boolean hasNext;

    public EvaluationsForReviewerResponse() {}

    @Getter
    static class EvaluationSummary {
        private Long id;
        private String username;
        private String imageUrl;
        private Float score;
        private String content;
    }

}

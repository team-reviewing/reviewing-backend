package project.reviewing.evaluation.command.application.response;

import lombok.Getter;
import org.springframework.data.domain.Slice;
import project.reviewing.evaluation.query.dao.data.EvaluationForReviewerData;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class EvaluationsForReviewerResponse {

    private final List<EvaluationSummary> evaluations;
    private final boolean hasNext;

    public static EvaluationsForReviewerResponse of(final Slice<EvaluationForReviewerData> evaluationForReviewerDataList) {
        return new EvaluationsForReviewerResponse(
                evaluationForReviewerDataList.stream()
                        .map(EvaluationSummary::from)
                        .collect(Collectors.toList()),
                evaluationForReviewerDataList.hasNext()
        );
    }

    private EvaluationsForReviewerResponse(final List<EvaluationSummary> evaluations, final boolean hasNext) {
        this.evaluations = evaluations;
        this.hasNext = hasNext;
    }

    @Getter
    public static class EvaluationSummary {

        private final Long id;
        private final String username;
        private final String imageUrl;
        private final Float score;
        private final String content;

        private static EvaluationSummary from(final EvaluationForReviewerData data) {
            return new EvaluationSummary(
                    data.getId(), data.getUsername(), data.getImageUrl(),
                    data.getScore(), data.getContent()
            );
        }

        private EvaluationSummary(
                final Long id, final String username, final String imageUrl, final Float score, final String content
        ) {
            this.id = id;
            this.username = username;
            this.imageUrl = imageUrl;
            this.score = score;
            this.content = content;
        }
    }

}

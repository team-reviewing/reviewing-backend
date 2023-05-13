package project.reviewing.evaluation.query.dao.data;

import lombok.Getter;

@Getter
public class EvaluationForReviewerData {

    private final Long id;
    private final String username;
    private final String imageUrl;
    private final Float score;
    private final String content;

    public EvaluationForReviewerData(
            final Long id, final String username, final String imageUrl, final Float score, final String content
    ) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.score = score;
        this.content = content;
    }
}

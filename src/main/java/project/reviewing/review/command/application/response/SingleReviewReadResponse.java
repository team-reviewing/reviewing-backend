package project.reviewing.review.command.application.response;

import lombok.Getter;
import project.reviewing.review.command.domain.Review;

@Getter
public class SingleReviewReadResponse {

    private final Long id;
    private final Long reviewerId;
    private final String title;
    private final String content;
    private final String prUrl;
    private final String status;

    public static SingleReviewReadResponse from(final Review review) {
        return new SingleReviewReadResponse(
                review.getId(), review.getReviewerId(), review.getTitle(),
                review.getContent(), review.getPrUrl(), review.getStatus().name()
        );
    }

    private SingleReviewReadResponse(
            final Long id, final Long reviewerId, final String title,
            final String content, final String prUrl, final String status
    ) {
        this.id = id;
        this.reviewerId = reviewerId;
        this.title = title;
        this.content = content;
        this.prUrl = prUrl;
        this.status = status;
    }
}

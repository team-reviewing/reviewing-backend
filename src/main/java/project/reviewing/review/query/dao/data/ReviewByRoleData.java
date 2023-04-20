package project.reviewing.review.query.dao.data;

import lombok.Getter;
import project.reviewing.review.command.domain.ReviewStatus;

@Getter
public class ReviewByRoleData {

    private final Long reviewId;
    private final String title;
    private final Long reviewerId;
    private final String status;
    private final Long memberId;
    private final String username;
    private final String imageUrl;

    public ReviewByRoleData(
            final Long reviewId, final String title, final Long reviewerId, final ReviewStatus status,
            final Long memberId, final String username, final String imageUrl
    ) {
        this.reviewId = reviewId;
        this.title = title;
        this.reviewerId = reviewerId;
        this.status = status.name();
        this.memberId = memberId;
        this.username = username;
        this.imageUrl = imageUrl;
    }
}

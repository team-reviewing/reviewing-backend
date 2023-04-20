package project.reviewing.review.query.dao.data;

import lombok.Getter;

@Getter
public class ReviewByRoleData {

    private final Long reviewId;
    private final String title;
    private final Long reviewerId;
    private final Long memberId;
    private final String username;
    private final String imageUrl;

    public ReviewByRoleData(
            final Long reviewId, final String title, final Long reviewerId,
            final Long memberId, final String username, final String imageUrl
    ) {
        this.reviewId = reviewId;
        this.title = title;
        this.reviewerId = reviewerId;
        this.memberId = memberId;
        this.username = username;
        this.imageUrl = imageUrl;
    }
}

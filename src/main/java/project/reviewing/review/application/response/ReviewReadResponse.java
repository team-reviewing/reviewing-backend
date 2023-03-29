package project.reviewing.review.application.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewReadResponse {

    private long id;
    private long reviewerId;
    private String title;
    private String content;
    private String prUrl;

    public ReviewReadResponse(
            final long id, final long reviewerId, final String title, final String content, final String prUrl
    ) {
        this.id = id;
        this.reviewerId = reviewerId;
        this.title = title;
        this.content = content;
        this.prUrl = prUrl;
    }
}

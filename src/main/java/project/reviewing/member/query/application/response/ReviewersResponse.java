package project.reviewing.member.query.application.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Slice;
import project.reviewing.member.query.dao.data.ReviewerData;

@Getter
public class ReviewersResponse {

    private final List<ReviewerResponse> reviewers;
    private final boolean hasNext;

    public static ReviewersResponse from(final Slice<ReviewerData> reviewerData) {
        return new ReviewersResponse(mapReviewerResponse(reviewerData), reviewerData.hasNext());
    }

    private static List<ReviewerResponse> mapReviewerResponse(final Slice<ReviewerData> reviewerData) {
        return reviewerData.getContent()
                .stream()
                .map(ReviewerResponse::from)
                .collect(Collectors.toList());
    }

    private ReviewersResponse(final List<ReviewerResponse> reviewers, final boolean hasNext) {
        this.reviewers = reviewers;
        this.hasNext = hasNext;
    }
}

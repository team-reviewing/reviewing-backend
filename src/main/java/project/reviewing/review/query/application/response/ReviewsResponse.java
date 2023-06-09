package project.reviewing.review.query.application.response;

import lombok.Getter;
import project.reviewing.review.query.dao.data.ReviewByRoleData;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReviewsResponse {

    private final List<ReviewSummary> reviews;

    public static ReviewsResponse from(List<ReviewByRoleData> reviewByRoleDataList) {
        return new ReviewsResponse(
                reviewByRoleDataList.stream()
                        .map(ReviewSummary::from)
                        .collect(Collectors.toList())
        );
    }

    private ReviewsResponse(final List<ReviewSummary> reviews) {
        this.reviews = reviews;
    }

    @Getter
    public static class ReviewSummary {
        private final Long id;
        private final String title;
        private final Long reviewerId;
        private final String status;
        private final MemberSummary member;

        private static ReviewSummary from(final ReviewByRoleData reviewByRoleData) {
            return new ReviewSummary(
                    reviewByRoleData.getReviewId(), reviewByRoleData.getTitle(),
                    reviewByRoleData.getReviewerId(), reviewByRoleData.getStatus(),
                    new MemberSummary(
                            reviewByRoleData.getMemberId(), reviewByRoleData.getUsername(), reviewByRoleData.getImageUrl()
                    )
            );
        }

        private ReviewSummary(
                final Long id, final String title, final Long reviewerId,
                final String status, final MemberSummary member
        ) {
            this.id = id;
            this.title = title;
            this.reviewerId = reviewerId;
            this.status = status;
            this.member = member;
        }

        @Getter
        public static class MemberSummary {
            private final Long id;
            private final String username;
            private final String imageUrl;

            private MemberSummary(final Long id, final String username, final String imageUrl) {
                this.id = id;
                this.username = username;
                this.imageUrl = imageUrl;
            }
        }
    }
}

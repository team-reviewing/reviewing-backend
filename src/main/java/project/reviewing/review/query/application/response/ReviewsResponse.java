package project.reviewing.review.query.application.response;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewsResponse {

    private List<ReviewSummary> reviews;

    public ReviewsResponse() {}

    public ReviewsResponse(final List<ReviewSummary> reviews) {
        this.reviews = reviews;
    }

    static class ReviewSummary {
        private final Long id;
        private final String title;
        private final Long reviewerId;
        private final MemberSummary member;

        public ReviewSummary(final Long id, final String title, final Long reviewerId, final MemberSummary member) {
            this.id = id;
            this.title = title;
            this.reviewerId = reviewerId;
            this.member = member;
        }

        static class MemberSummary {
            private final Long id;
            private final String username;
            private final String imageUrl;

            public MemberSummary(final Long id, final String username, final String imageUrl) {
                this.id = id;
                this.username = username;
                this.imageUrl = imageUrl;
            }
        }
    }
}

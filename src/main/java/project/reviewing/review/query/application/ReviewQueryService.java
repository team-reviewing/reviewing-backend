package project.reviewing.review.query.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.review.query.application.response.ReviewsResponse;

@Transactional(readOnly = true)
@Service
public class ReviewQueryService {

    public ReviewsResponse findReviewsByRole(final Long memberId, final String role) {
        return new ReviewsResponse();
    }
}

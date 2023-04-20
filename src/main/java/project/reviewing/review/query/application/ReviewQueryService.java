package project.reviewing.review.query.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.review.presentation.data.RoleInReview;
import project.reviewing.review.query.application.response.ReviewsResponse;
import project.reviewing.review.query.dao.ReviewsDAO;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewQueryService {

    private final ReviewsDAO reviewsDAO;

    public ReviewsResponse findReviewsByRole(final Long memberId, final RoleInReview role) {
        return ReviewsResponse.from(reviewsDAO.findReviewsByRole(memberId, role));
    }
}

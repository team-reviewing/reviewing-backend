package project.reviewing.review.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ReviewRepository extends Repository<Review, Long> {

    Review save(Review entity);
    Optional<Review> findById(Long id);
    Optional<Review> findByRevieweeIdAndReviewerId(Long revieweeId, Long reviewerId);
    Boolean existsByRevieweeIdAndReviewerId(Long revieweeId, Long reviewerId);
}

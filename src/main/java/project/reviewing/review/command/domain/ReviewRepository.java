package project.reviewing.review.command.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends Repository<Review, Long> {

    Review save(Review entity);
    Optional<Review> findById(Long id);
    Optional<Review> findByRevieweeIdAndReviewerId(Long revieweeId, Long reviewerId);
    void delete(Review entity);
}

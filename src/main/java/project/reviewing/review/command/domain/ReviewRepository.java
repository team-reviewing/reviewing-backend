package project.reviewing.review.command.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends Repository<Review, Long> {

    Review save(Review entity);
    List<Review> findAll();
    Optional<Review> findById(Long id);
    Optional<Review> findByRevieweeIdAndReviewerId(Long revieweeId, Long reviewerId);
    void delete(Review entity);

    @Transactional
    @Modifying
    @Query(
            "UPDATE Review r " +
            "SET r.status = :refused, r.statusSetAt = CURRENT_TIMESTAMP, r.version = r.version + 1 " +
            "WHERE (:startId < r.id AND r.id <= :endId) " +
                    "AND (" +
                        "(r.status = :created AND r.statusSetAt <= :createdExpiredTime) " +
                        "OR (r.status = :accepted AND r.statusSetAt <= :acceptedExpiredTime) " +
                    ")"
    )
    int updateExpiredReviews(
            Long startId, Long endId,
            ReviewStatus refused, ReviewStatus created, ReviewStatus accepted,
            LocalDateTime createdExpiredTime, LocalDateTime acceptedExpiredTime
    );

    @Transactional
    @Modifying
    @Query(
            "DELETE FROM Review r " +
            "WHERE (:startId < r.id AND r.id <= :endId) " +
                    "AND (" +
                        "(r.status = :refused AND r.statusSetAt <= :refusedExpiredTime) " +
                        "OR (r.status = :approved AND r.statusSetAt <= :approvedExpiredTime) " +
                        "OR (r.status = :evaluated AND r.statusSetAt <= :evaluatedExpiredTime) " +
                    ")"
    )
    int deleteExpiredReviews(
            Long startId, Long endId,
            ReviewStatus refused, ReviewStatus approved, ReviewStatus evaluated,
            LocalDateTime refusedExpiredTime, LocalDateTime approvedExpiredTime, LocalDateTime evaluatedExpiredTime
    );
}

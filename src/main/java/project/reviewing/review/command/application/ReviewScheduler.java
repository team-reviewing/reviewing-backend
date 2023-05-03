package project.reviewing.review.command.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ReviewScheduler {

    private final ReviewRepository reviewRepository;

    @Transactional
    @Scheduled(initialDelay = 0, fixedRate = 5000)
    void checkExpirationForAllReview() {
        List<Review> reviews = reviewRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (Review review : reviews) {
            if (review.isApproved() && review.getStatusSetAt().plusDays(3).isBefore(now)) {
                reviewRepository.delete(review);
            }
        }
    }
}

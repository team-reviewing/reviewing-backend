package project.reviewing.review.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewRepository;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ReviewScheduler {

    private final ReviewRepository reviewRepository;

    @Transactional
    @Scheduled(cron = "${schedule.cron}")
    public void checkExpirationForAllReview() {
        final List<Review> reviews = reviewRepository.findAll();

        for (Review review : reviews) {
            if (review.isExpiredInRefusedStatus() || review.isExpiredInApprovedStatus()) {
                reviewRepository.delete(review);
            }
        }
    }
}

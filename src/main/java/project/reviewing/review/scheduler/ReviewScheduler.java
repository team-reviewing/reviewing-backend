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

        System.out.println("스케쥴러 안 " + Thread.currentThread().getId() + " 리뷰 개수 : " + reviews.size());
        for (Review review : reviews) {
            if (review.isExpiredInApprovedStatus()) {
                reviewRepository.delete(review);
            }
        }
    }
}

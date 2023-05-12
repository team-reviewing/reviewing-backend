package project.reviewing.integration.review.scheduler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.SpyBean;
import project.reviewing.common.util.Time;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.review.command.domain.ReviewStatus;
import project.reviewing.review.scheduler.ReviewScheduler;
import project.reviewing.review.command.domain.Review;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("ReviewScheduler는 ")
public class ReviewSchedulerTest extends IntegrationTest {

    @SpyBean
    private ReviewScheduler reviewScheduler;

    @Mock
    private Time time;

    @DisplayName("주기적으로 checkExpirationForAllReview() Method를 호출한다.")
    @Test
    void workScheduledTask() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        verify(reviewScheduler, atLeast(2)).checkExpirationForAllReview();
    }

    @DisplayName("완료/평가 된 리뷰 중 완료 시점부터 일정 기간이 지나면 삭제한다.")
    @Test
    void deleteExpiredApprovedOrEvaluatedReviews() {
        // given
        when(time.now()).thenReturn(LocalDateTime.now().minusDays(10));

        final Review approvedReview = createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));
        final Review evaluatedReview = createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));
        createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));

        approvedReview.approve(time);
        evaluatedReview.approve(time);
        evaluatedReview.evaluate();
        entityManager.merge(approvedReview);
        entityManager.merge(evaluatedReview);
        entityManager.flush();
        entityManager.clear();

        // when
        reviewScheduler.checkExpirationForAllReview();

        // then
        assertThat(reviewRepository.findAll()).hasSize(1);
    }

    @DisplayName("거절 된 리뷰 중 거절 시점부터 일정 기간이 지나면 삭제한다.")
    @Test
    void deleteExpiredRefusedReviews() {
        // given
        when(time.now()).thenReturn(LocalDateTime.now().minusDays(10));

        final Review review = createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));
        createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));

        review.refuse(time);
        entityManager.merge(review);
        entityManager.flush();
        entityManager.clear();

        // when
        reviewScheduler.checkExpirationForAllReview();

        // then
        assertThat(reviewRepository.findAll()).hasSize(1);
    }

    @DisplayName("생성/수락 된 리뷰 중 생성/수락 시점부터 일정 기간이 지나면 거절 상태로 변경한다.")
    @Test
    void refuseExpiredCreatedOrAcceptedReviews() {
        // given
        when(time.now()).thenReturn(LocalDateTime.now().minusDays(10));

        final Review review = createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));
        createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));

        review.accept(time);
        entityManager.merge(review);
        entityManager.flush();
        entityManager.clear();

        // when
        reviewScheduler.checkExpirationForAllReview();

        // then
        final List<Review> reviews = reviewRepository.findAll();

        assertThat(reviews).hasSize(2);

        for (Review r : reviews) {
            assertThat(r.getStatus()).isEqualTo(ReviewStatus.REFUSED);
        }
    }
}

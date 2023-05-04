package project.reviewing.integration.review.scheduler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.SpyBean;
import project.reviewing.common.util.Time;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.review.scheduler.ReviewScheduler;
import project.reviewing.review.command.domain.Review;

import java.time.LocalDateTime;

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

    @DisplayName("완료 된 리뷰 중 일정 기간이 지나면 삭제할 수 있다.")
    @Test
    void deleteExpiredApprovedReviews() {
        // given
        when(time.now()).thenReturn(LocalDateTime.now().minusDays(4));

        final Review review = createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));
        createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));
        createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));

        review.accept(time);
        review.approve(time);
        entityManager.merge(review);
        entityManager.flush();
        entityManager.clear();

        // when
        reviewScheduler.checkExpirationForAllReview();

        // then
        assertThat(reviewRepository.findAll()).hasSize(2);
    }

    @DisplayName("거절 된 리뷰 중 일정 기간이 지나면 삭제할 수 있다.")
    @Test
    void deleteExpiredRefusedReviews() {
        // given
        when(time.now()).thenReturn(LocalDateTime.now().minusDays(4));

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
}

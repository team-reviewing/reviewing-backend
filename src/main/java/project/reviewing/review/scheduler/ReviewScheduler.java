package project.reviewing.review.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import project.reviewing.common.util.Time;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewRepository;
import project.reviewing.review.command.domain.ReviewStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Component
public class ReviewScheduler {

    private static final int BATCH_SIZE = 10000;

    private final ReviewRepository reviewRepository;
    private final Time time;

    @PersistenceContext
    private final EntityManager em;

    @Scheduled(cron = "${schedule.cron}")
    public void handleExpiredReviews() {

        LocalDateTime createdExpired = time.now().minusDays(ReviewStatus.CREATED.getExpirePeriod());
        LocalDateTime acceptedExpired = time.now().minusDays(ReviewStatus.ACCEPTED.getExpirePeriod());
        LocalDateTime refusedExpired = time.now().minusDays(ReviewStatus.REFUSED.getExpirePeriod());
        LocalDateTime approvedExpired = time.now().minusDays(ReviewStatus.APPROVED.getExpirePeriod());
        LocalDateTime evaluatedExpired = time.now().minusDays(ReviewStatus.EVALUATED.getExpirePeriod());

        List<Long> list = em.createQuery("SELECT id FROM Review r", Long.class)
                .getResultList();

        Collections.sort(list);

        long startId = 0;
        long endId = 0;
        long maxIdx = (list.size() % BATCH_SIZE == 0) ? 0 : list.size() + BATCH_SIZE;

        for (int i = BATCH_SIZE - 1; i < maxIdx; i += BATCH_SIZE) {
            if (i < list.size()) {
                endId = list.get(i);
            } else {
                endId = list.get(list.size() - 1);
            }

            reviewRepository.updateExpiredReviews(
                    startId, endId, ReviewStatus.REFUSED, ReviewStatus.CREATED, ReviewStatus.ACCEPTED,
                    createdExpired, acceptedExpired
            );
            reviewRepository.deleteExpiredReviews(
                    startId, endId, ReviewStatus.REFUSED, ReviewStatus.APPROVED, ReviewStatus.EVALUATED,
                    refusedExpired, approvedExpired, evaluatedExpired
            );
            startId = endId;
        }
    }
}

package project.reviewing.concurrency.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import project.reviewing.common.util.Time;
import project.reviewing.concurrency.ConcurrencyRunner;
import project.reviewing.concurrency.DatabaseCleaner;
import project.reviewing.member.command.domain.*;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewRepository;
import project.reviewing.review.command.domain.ReviewStatus;
import project.reviewing.review.exception.ReviewNotFoundException;
import project.reviewing.review.scheduler.ReviewScheduler;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayName("Review Entity의 동시성 관련 상황에서는 ")
@ActiveProfiles({"schedulerExclusion", "databaseCleaner"})
@SpringBootTest
public class ReviewConcurrencyTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Time time;

    @Mock
    private Time customTime;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void init() {
        databaseCleaner.execute();
    }

    @DisplayName("Review Entity를 동시에 수정하면 처음 수정 내용만 반영되고 version이 하나만 증가한다.")
    @Test
    void ChangeReviewStatusConcurrencyTest() {
        // given
        int threadCnt = 3;
        ConcurrencyRunner runner = new ConcurrencyRunner(threadCnt);

        Member reviewee = new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom");
        Member reviewerMember = new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor");
        Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글");

        reviewerMember.register(reviewer);

        memberRepository.save(reviewee);
        memberRepository.save(reviewerMember);

        Review review = Review.assign(
                reviewee.getId(), reviewer.getId(), "제목", "본문",
                "github.com/bboor/project/pull/1", reviewerMember.getId(), true, time
        );

        reviewRepository.save(review);

        // when
        while (threadCnt-- > 0) {
            runner.execute(() -> {
                EntityManager em = emf.createEntityManager();
                EntityTransaction transaction = em.getTransaction();

                try {
                    transaction.begin();
                    review.evaluate();
                    em.merge(review);
                    em.flush();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                } finally {
                    em.close();
                }
            }, 0);
        }
        runner.await(500);

        //then
        Review updatedReview = reviewRepository.findById(review.getId())
                .orElseThrow(ReviewNotFoundException::new);

        assertThat(updatedReview.getVersion()).isEqualTo(1);
        assertThat(updatedReview.getStatus()).isEqualTo(ReviewStatus.EVALUATED);
    }

    @DisplayName(
            "ACCEPT 상태에서 만료된 Review Entity를 리뷰어가 APPROVE 상태로 변경하는 동시에 " +
                    "Scheduler가 REFUSED 상태로 변경하는 상황에서" +
                    "첫 번째 로직에서 Entity 조회 후 로직이 완료되기 전에 두번째 로직이 완료되는 경우, " +
                    "첫 번째 로직이 실패하고 상태는 REFUSED 상태로 변경된다."
    )
    @Test
    void ChangeReviewStatusAndSchedulerTest() {
        // given
        int threadCnt = 2;
        ConcurrencyRunner runner = new ConcurrencyRunner(threadCnt);
        ReviewScheduler reviewScheduler = new ReviewScheduler(reviewRepository, time, emf.createEntityManager());

        given(customTime.now()).willReturn(LocalDateTime.now().minusDays(10));

        Member reviewee = new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom");
        Member reviewerMember = new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor");
        Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글");

        reviewerMember.register(reviewer);

        memberRepository.save(reviewee);
        memberRepository.save(reviewerMember);

        Review review = Review.assign(
                reviewee.getId(), reviewer.getId(), "제목", "본문",
                "github.com/bboor/project/pull/1", reviewerMember.getId(), true, time
        );

        review.accept(customTime);
        reviewRepository.save(review);

        // when
        runner.execute(() -> {
            EntityManager em = emf.createEntityManager();
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();
                Review approvingReview = em.find(Review.class, review.getId());
                Thread.sleep(500);
                approvingReview.approve(time);
                em.flush();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            } finally {
                em.close();
            }
        }, 0);
        runner.execute(reviewScheduler::handleExpiredReviews, 0);
        runner.await(500);

        // then
        Review updatedReview = reviewRepository.findById(review.getId())
                .orElseThrow(ReviewNotFoundException::new);

        assertThat(updatedReview.getVersion()).isEqualTo(1);
        assertThat(updatedReview.getStatus()).isEqualTo(ReviewStatus.REFUSED);
    }
}
package project.reviewing.concurrency.review;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import project.reviewing.common.util.Time;
import project.reviewing.concurrency.ConcurrencyRunner;
import project.reviewing.member.command.domain.*;
import project.reviewing.review.command.application.ReviewService;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewRepository;
import project.reviewing.review.exception.ReviewNotFoundException;

import javax.persistence.*;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestEntityManager
@DisplayName("Review Entity의 동시성 관련 상황에서는 ")
public class ReviewConcurrencyTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Time time;

    @PersistenceUnit
    private EntityManagerFactory emf;

    @DisplayName("Review Entity를 동시에 수정하면 처음 수정 내용만 반영되고 version이 하나만 증가한다.")
    @Test
    void ChangeReviewStatusConcurrencyTest() throws InterruptedException {
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
                    em.merge(review);
                    review.evaluate();
                    em.flush();
                    transaction.commit();
                } catch (Exception e) {
                    transaction.rollback();
                } finally {
                    em.close();
                }
                em.close();
            }, 0);
        }
        runner.await(500);

        Review updatedReview = reviewRepository.findById(review.getId())
                .orElseThrow(ReviewNotFoundException::new);

        //then
        assertThat(updatedReview.getVersion()).isEqualTo(1);
    }
}

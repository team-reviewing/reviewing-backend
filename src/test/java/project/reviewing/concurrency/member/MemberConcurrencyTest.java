package project.reviewing.concurrency.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import project.reviewing.common.util.Time;
import project.reviewing.concurrency.ConcurrencyRunner;
import project.reviewing.concurrency.DatabaseCleaner;
import project.reviewing.evaluation.command.application.EvaluationService;
import project.reviewing.evaluation.presentation.request.EvaluationCreateRequest;
import project.reviewing.member.command.application.MemberService;
import project.reviewing.member.command.domain.*;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.review.command.application.ReviewService;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewRepository;
import project.reviewing.review.presentation.request.ReviewCreateRequest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Member Entity의 동시성 관련 상황에서는 ")
@ActiveProfiles("databaseCleaner")
@SpringBootTest
public class MemberConcurrencyTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private EvaluationService evaluationService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private Time time;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @BeforeEach
    void init() {
        databaseCleaner.execute();
    }

    @DisplayName(
            "동일한 Member에 대해 Member의 리뷰어 활동 상태를 수정하는 도중에 Review를 생성할 경우, "
                    + "활동 상태가 변경 된 후 Review 생성 로직 내의 리뷰어 활동 상태 검증이 진행된다."
    )
    @Test
    void CreateReviewAndUpdateMemberTest() {
        int threadCnt = 2;
        ConcurrencyRunner runner = new ConcurrencyRunner(threadCnt);

        // given
        Member reviewee = new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom");
        Member reviewerMember = new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor");
        Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글");
        ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
        );

        reviewerMember.register(reviewer);

        memberRepository.save(reviewee);
        memberRepository.save(reviewerMember);

        // when
        runner.execute(() -> memberService.changeReviewerStatus(reviewerMember.getId()), 0);
        runner.execute(() -> reviewService.createReview(reviewee.getId(), reviewer.getId(), reviewCreateRequest), 500);
        runner.await(500);

        //then
        Member m = memberRepository.findById(reviewerMember.getId())
                .orElseThrow(MemberNotFoundException::new);

        assertThat(reviewRepository.findAll()).isEmpty();
        assertThat(m.isReviewer()).isFalse();
    }

    @DisplayName("동시에 동일한 리뷰어에게 평가하고 점수를 업데이트 하는 경우 차례대로 전부 반영된다.")
    @Test
    void UpdateScoreConcurrencyTest() {
        int threadCnt = 2;
        ConcurrencyRunner runner = new ConcurrencyRunner(threadCnt);

        // given
        Member reviewerMember = new Member(1L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor");
        Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글");
        Member reviewee1 = new Member(2L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom");
        Member reviewee2 = new Member(3L, "Jack", "Jack@gmail.com", "imageUrl", "https://github.com/Jack");

        reviewerMember.register(reviewer);

        memberRepository.save(reviewerMember);
        memberRepository.save(reviewee1);
        memberRepository.save(reviewee2);

        Review review1 = Review.assign(
                reviewee1.getId(), reviewer.getId(), "제목", "본문",
                "github.com/bboor/project/pull/1", reviewerMember.getId(), true, time
        );
        Review review2 = Review.assign(
                reviewee2.getId(), reviewer.getId(), "제목", "본문",
                "github.com/bboor/project/pull/1", reviewerMember.getId(), true, time
        );

        review1.approve(time);
        review2.approve(time);

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        EvaluationCreateRequest evaluationCreateRequest1 = new EvaluationCreateRequest(
                review1.getId(), 2.0f, "본문1"
        );
        EvaluationCreateRequest evaluationCreateRequest2 = new EvaluationCreateRequest(
                review2.getId(), 4.0f, "본문2"
        );

        // when
        runner.execute(
                () -> evaluationService.createEvaluation(reviewee1.getId(), reviewer.getId(), evaluationCreateRequest1), 0
        );
        runner.execute(
                () -> evaluationService.createEvaluation(reviewee2.getId(), reviewer.getId(), evaluationCreateRequest2), 0
        );
        runner.await(500);

        //then
        Member m = memberRepository.findById(reviewerMember.getId())
                .orElseThrow(MemberNotFoundException::new);

        assertThat(m.getReviewer().getEvaluationCnt()).isEqualTo(2);
        assertThat(m.getReviewer().getScore()).isEqualTo(3.0f);
    }
}

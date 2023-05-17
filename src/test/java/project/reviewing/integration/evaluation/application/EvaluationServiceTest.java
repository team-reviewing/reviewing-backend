package project.reviewing.integration.evaluation.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.evaluation.command.application.EvaluationService;
import project.reviewing.evaluation.command.domain.Evaluation;
import project.reviewing.evaluation.exception.EvaluationNotFoundException;
import project.reviewing.evaluation.exception.InvalidEvaluationException;
import project.reviewing.evaluation.presentation.request.EvaluationCreateRequest;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.exception.InvalidReviewException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("EvaluationService는 ")
public class EvaluationServiceTest extends IntegrationTest {

    private EvaluationService evaluationService;

    @BeforeEach
    void setUp() {
        evaluationService = new EvaluationService(memberRepository, reviewRepository, evaluationRepository);
    }

    @DisplayName("리뷰 평가 생성 시 ")
    @Nested
    class EvaluationCreateTest {

        @DisplayName("정상적으로 리뷰 평가가 생성된다.")
        @Test
        void validCreateEvaluation() {
            // given
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Review review1 = createReview(
                    Review.assign(
                            reviewee.getId(), reviewerMember.getReviewer().getId(),
                            "제목1", "본문1", "prUrl1", reviewerMember.getId(), reviewerMember.isReviewer(), time
                    ));
            final Review review2 = createReview(
                    Review.assign(
                            reviewee.getId(), reviewerMember.getReviewer().getId(),
                            "제목2", "본문2", "prUrl2", reviewerMember.getId(), reviewerMember.isReviewer(), time
                    ));
            final EvaluationCreateRequest request = new EvaluationCreateRequest(review2.getId(), 3.5F, "평가 내용");

            review1.approve(time);
            review2.approve(time);
            entityManager.merge(review1);
            entityManager.merge(review2);
            entityManager.flush();
            entityManager.clear();

            // when, then
            assertDoesNotThrow(
                    () -> evaluationService.createEvaluation(
                            reviewee.getId(), reviewerMember.getReviewer().getId(), request
                    ));

            final Evaluation createdEvaluation = evaluationRepository.findByReviewId(review2.getId())
                    .orElseThrow(EvaluationNotFoundException::new);

            assertAll(
                    () -> assertThat(createdEvaluation.getReviewerId()).isEqualTo(reviewerMember.getReviewer().getId()),
                    () -> assertThat(createdEvaluation.getRevieweeId()).isEqualTo(reviewee.getId()),
                    () -> assertThat(createdEvaluation.getReviewId()).isEqualTo(review2.getId()),
                    () -> assertThat(createdEvaluation.getScore()).isEqualTo(request.getScore()),
                    () -> assertThat(createdEvaluation.getContent()).isEqualTo(request.getContent())
            );
        }

        @DisplayName("이미 해당 리뷰에 대한 평가가 생성되었다면 예외 반환한다.")
        @Test
        void createWithAlreadyExistEvaluation() {
            // given
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Review review = createReview(
                    Review.assign(
                            reviewee.getId(), reviewerMember.getReviewer().getId(),
                            "제목", "본문", "prUrl", reviewerMember.getId(), reviewerMember.isReviewer(), time
                    ));
            createEvaluation(
                    new Evaluation(
                            review.getId(), reviewee.getId(), reviewerMember.getReviewer().getId(), 3.5F, "평가 내용"
                    ));
            final EvaluationCreateRequest request = new EvaluationCreateRequest(review.getId(), 3.5F, "평가 내용");

            review.approve(time);
            entityManager.merge(review);
            entityManager.flush();
            entityManager.clear();

            // when, then
            assertThatThrownBy(
                    () -> evaluationService.createEvaluation(
                            reviewee.getId(), reviewerMember.getReviewer().getId(), request
                    ))
                    .isInstanceOf(InvalidEvaluationException.class)
                    .hasMessage(ErrorType.ALREADY_EVALUATED.getMessage());
        }

        @DisplayName("해당 리뷰를 작성한 리뷰이가 아니라면 예외 반환한다.")
        @Test
        void createWithNotRevieweeOfReview() {
            // given
            final Long invalidRevieweeId = -1L;
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Review review = createReview(
                    Review.assign(
                            reviewee.getId(), reviewerMember.getReviewer().getId(),
                            "제목", "본문", "prUrl", reviewerMember.getId(), reviewerMember.isReviewer(), time
                    ));
            final EvaluationCreateRequest request = new EvaluationCreateRequest(review.getId(), 3.5F, "평가 내용");

            review.approve(time);
            entityManager.merge(review);
            entityManager.flush();
            entityManager.clear();

            // when, then
            assertThatThrownBy(
                    () -> evaluationService.createEvaluation(
                            invalidRevieweeId, reviewerMember.getReviewer().getId(), request
                    ))
                    .isInstanceOf(InvalidReviewException.class)
                    .hasMessage(ErrorType.NOT_REVIEWEE_OF_REVIEW.getMessage());
        }

        @DisplayName("해당 리뷰를 요청받은 리뷰어가 아니라면 예외 반환한다.")
        @Test
        void createWithNotReviewerOfReview() {
            // given
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Member invalidReviewerMember = createMemberAndRegisterReviewer(
                    new Member(3L, "sioh", "sioh@gmail.com", "imageUrl", "https://github.com/sioh"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Review review = createReview(
                    Review.assign(
                            reviewee.getId(), reviewerMember.getReviewer().getId(),
                            "제목", "본문", "prUrl", reviewerMember.getId(), reviewerMember.isReviewer(), time
                    ));
            final EvaluationCreateRequest request = new EvaluationCreateRequest(review.getId(), 3.5F, "평가 내용");

            review.approve(time);
            entityManager.merge(review);
            entityManager.flush();
            entityManager.clear();

            // when, then
            assertThatThrownBy(
                    () -> evaluationService.createEvaluation(
                            reviewee.getId(), invalidReviewerMember.getReviewer().getId(), request
                    ))
                    .isInstanceOf(InvalidReviewException.class)
                    .hasMessage(ErrorType.NOT_REVIEWER_OF_REVIEW.getMessage());
        }
    }
}

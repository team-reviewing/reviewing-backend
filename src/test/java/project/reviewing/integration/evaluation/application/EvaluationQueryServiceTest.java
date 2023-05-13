package project.reviewing.integration.evaluation.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.evaluation.command.domain.Evaluation;
import project.reviewing.evaluation.exception.EvaluationNotFoundException;
import project.reviewing.evaluation.query.application.EvaluationQueryService;
import project.reviewing.evaluation.query.application.response.SingleEvaluationResponse;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.review.command.domain.Review;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class EvaluationQueryServiceTest extends IntegrationTest {

    private EvaluationQueryService evaluationQueryService;

    @BeforeEach
    void setUp() {
        evaluationQueryService = new EvaluationQueryService(evaluationRepository);
    }

    @DisplayName("단일 리뷰 평가 조회 시 ")
    @Nested
    class SingleEvaluationFindTest {

        @DisplayName("정상적으로 조회된다.")
        @Test
        void validFindSingleEvaluation() {
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
            final Evaluation evaluation = createEvaluation(
                    new Evaluation(
                            review.getId(), reviewee.getId(), reviewerMember.getReviewer().getId(), 3.5F, "평가 내용"
                    ));

            // when, then
            final SingleEvaluationResponse response = evaluationQueryService.findSingleEvaluationByReviewId(review.getId());

            assertAll(
                    () -> assertThat(response.getId()).isEqualTo(evaluation.getId()),
                    () -> assertThat(response.getScore()).isEqualTo(evaluation.getScore()),
                    () -> assertThat(response.getContent()).isEqualTo(evaluation.getContent())
            );
        }

        @DisplayName("평가 데이터가 없으면 예외 반환한다.")
        @Test
        void findWithNotRevieweeOfReview() {
            // given
            final Long invalidReviewId = -1L;

            // when, then
            assertThatThrownBy(() -> evaluationQueryService.findSingleEvaluationByReviewId(invalidReviewId))
                    .isInstanceOf(EvaluationNotFoundException.class)
                    .hasMessage(ErrorType.EVALUATION_NOT_FOUND.getMessage());
        }
    }
}

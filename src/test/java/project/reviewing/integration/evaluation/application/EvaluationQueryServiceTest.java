package project.reviewing.integration.evaluation.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.evaluation.command.application.response.EvaluationsForReviewerResponse;
import project.reviewing.evaluation.command.domain.Evaluation;
import project.reviewing.evaluation.exception.EvaluationNotFoundException;
import project.reviewing.evaluation.query.application.EvaluationQueryService;
import project.reviewing.evaluation.query.application.response.SingleEvaluationResponse;
import project.reviewing.evaluation.query.dao.data.EvaluationForReviewerData;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.member.exception.InvalidMemberException;
import project.reviewing.member.exception.MemberNotFoundException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("EvaluationQueryService는 ")
public class EvaluationQueryServiceTest extends IntegrationTest {

    private EvaluationQueryService evaluationQueryService;

    @BeforeEach
    void setUp() {
        evaluationQueryService = new EvaluationQueryService(evaluationRepository, evaluationsDAO, memberRepository);
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
            final Long reviewId = 1L;

            final Evaluation evaluation = createEvaluation(
                    new Evaluation(
                            reviewerMember.getReviewer().getId(), reviewee.getId(), reviewId, 3.5F, "평가 내용"
                    ));

            // when, then
            final SingleEvaluationResponse response = evaluationQueryService.findSingleEvaluationByReviewId(reviewId);

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

    @DisplayName("특정 리뷰어의 리뷰 평가 목록 조회 시 ")
    @Nested
    class EvaluationsForReviewerFindTest {

        @DisplayName("정상적으로 조회된다.")
        @Test
        void validFindEvaluations() {
            final Member reviewee1 = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewee2 = createMember(new Member(2L, "J", "J@gmail.com", "imageUrl", "https://github.com/J"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(3L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Pageable pageable = PageRequest.of(0, 2);

            createEvaluation(new Evaluation(reviewerMember.getReviewer().getId(), reviewee1.getId(), 1L, 1.5F, "평가1"));
            createEvaluation(new Evaluation(reviewerMember.getReviewer().getId(), reviewee2.getId(), 2L, 2.0F, "평가2"));

            EvaluationsForReviewerResponse expectedResponse = EvaluationsForReviewerResponse.of(
                    new SliceImpl<>(
                            List.of(
                                    new EvaluationForReviewerData(
                                            1L, reviewee1.getUsername(), reviewee1.getImageUrl(), 1.5F, "평가1"
                                    ),
                                    new EvaluationForReviewerData(
                                            2L, reviewee2.getUsername(), reviewee2.getImageUrl(), 2.0F, "평가2"
                                    )
                            ), pageable, false)
            );

            EvaluationsForReviewerResponse response = evaluationQueryService.findEvaluationsForReviewerInPage(
                    reviewerMember.getReviewer().getId(), PageRequest.of(0, 2)
            );

            assertAll(
                    () -> assertThat(response.getEvaluations()).usingRecursiveFieldByFieldElementComparator()
                            .containsAll(expectedResponse.getEvaluations()),
                    () -> assertThat(response.isHasNext()).isEqualTo(expectedResponse.isHasNext())
            );
        }
    }

    @DisplayName("내 리뷰 평가 목록 조회 시 ")
    @Nested
    class MyEvaluationsFindTest {

        @DisplayName("정상적으로 조회된다.")
        @Test
        void validFindMyEvaluations() {
            final Member reviewee1 = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewee2 = createMember(new Member(2L, "J", "J@gmail.com", "imageUrl", "https://github.com/J"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(3L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Pageable pageable = PageRequest.of(0, 2);

            createEvaluation(new Evaluation(reviewerMember.getReviewer().getId(), reviewee1.getId(), 1L, 1.5F, "평가1"));
            createEvaluation(new Evaluation(reviewerMember.getReviewer().getId(), reviewee2.getId(), 2L, 2.0F, "평가2"));

            EvaluationsForReviewerResponse expectedResponse = EvaluationsForReviewerResponse.of(
                    new SliceImpl<>(
                            List.of(
                                    new EvaluationForReviewerData(
                                            1L, reviewee1.getUsername(), reviewee1.getImageUrl(), 1.5F, "평가1"
                                    ),
                                    new EvaluationForReviewerData(
                                            2L, reviewee2.getUsername(), reviewee2.getImageUrl(), 2.0F, "평가2"
                                    )
                            ), pageable, false)
            );

            EvaluationsForReviewerResponse response = evaluationQueryService.findEvaluationsForReviewerInPage(
                    reviewerMember.getReviewer().getId(), PageRequest.of(0, 2)
            );

            assertAll(
                    () -> assertThat(response.getEvaluations()).usingRecursiveFieldByFieldElementComparator()
                            .containsAll(expectedResponse.getEvaluations()),
                    () -> assertThat(response.isHasNext()).isEqualTo(expectedResponse.isHasNext())
            );
        }

        @DisplayName("유저 정보가 없으면 예외 반환한다.")
        @Test
        void findMyEvaluationsWithNotExistMember() {
            final Long invalidMemberId = -1L;

            assertThatThrownBy(
                    () -> evaluationQueryService.findMyEvaluationsInPage(invalidMemberId, PageRequest.of(0, 2))
            )
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
        }

        @DisplayName("리뷰어를 등록하지 않았다면 예외 반환한다.")
        @Test
        void findMyEvaluationsWithNotRegisterReviewer() {
            // given
            final Member reviewerMember = createMember(
                    new Member(3L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor")
            );

            // when, then
            assertThatThrownBy(
                    () -> evaluationQueryService.findMyEvaluationsInPage(reviewerMember.getId(), PageRequest.of(0, 2))
            )
                    .isInstanceOf(InvalidMemberException.class)
                    .hasMessage(ErrorType.DO_NOT_REGISTERED.getMessage());
        }
    }
}

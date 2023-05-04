package project.reviewing.integration.review.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.review.command.application.ReviewService;
import project.reviewing.review.command.application.response.SingleReviewReadResponse;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewStatus;
import project.reviewing.review.exception.InvalidReviewException;
import project.reviewing.review.exception.ReviewNotFoundException;
import project.reviewing.review.presentation.request.ReviewCreateRequest;
import project.reviewing.review.presentation.request.ReviewUpdateRequest;

import java.util.Set;

@DisplayName("ReviewService 는 ")
public class ReviewServiceTest extends IntegrationTest {

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(reviewRepository, reviewDAO, memberRepository, time);
    }

    @DisplayName("리뷰 생성 시 ")
    @Nested
    class ReviewCreateTest {

        @DisplayName("정상적으로 새 리뷰가 생성된다.")
        @Test
        void validCreateReview() {
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            reviewService.createReview(reviewee.getId(), reviewerMember.getReviewer().getId(), reviewCreateRequest);
            entityManager.clear();

            final Review newReview = reviewRepository
                    .findByRevieweeIdAndReviewerId(reviewee.getId(), reviewerMember.getReviewer().getId())
                    .orElseThrow(ReviewNotFoundException::new);
            assertAll(
                    () -> assertThat(newReview.getTitle()).isEqualTo(reviewCreateRequest.getTitle()),
                    () -> assertThat(newReview.getContent()).isEqualTo(reviewCreateRequest.getContent()),
                    () -> assertThat(newReview.getPrUrl()).isEqualTo(reviewCreateRequest.getPrUrl())
            );
        }

        @DisplayName("동일 리뷰어에게 요청한 완료된 리뷰가 존재할 때 정상적으로 새 리뷰가 생성된다.")
        @Test
        void createAlreadyExistReviewToSameReviewerWithApproved() {
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            final Review review = createReview(reviewCreateRequest.toEntity(
                    reviewee.getId(), reviewerMember.getReviewer().getId(),
                    reviewerMember.getId(), reviewerMember.isReviewer(), time)
            );

            reviewService.acceptReview(reviewerMember.getId(), review.getId());
            reviewService.approveReview(reviewerMember.getId(), review.getId());
            entityManager.flush();
            entityManager.clear();

            assertDoesNotThrow(() -> reviewService.createReview(
                    reviewee.getId(), reviewerMember.getReviewer().getId(), reviewCreateRequest
            ));
        }

        @DisplayName("동일 리뷰어에게 요청한 완료되지 않은 리뷰가 이미 존재한다면 예외 발생한다.")
        @Test
        void createAlreadyExistReviewToSameReviewer() {
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            createReview(reviewCreateRequest.toEntity(
                            reviewee.getId(), reviewerMember.getReviewer().getId(),
                            reviewerMember.getId(), reviewerMember.isReviewer(), time)
            );

            assertThatThrownBy(() -> reviewService.createReview(
                    reviewee.getId(), reviewerMember.getReviewer().getId(), reviewCreateRequest
                    ))
                    .isInstanceOf(InvalidReviewException.class)
                    .hasMessage(ErrorType.ALREADY_REQUESTED.getMessage());
        }

        @DisplayName("리뷰어의 회원 정보가 없으면 예외 발생한다.")
        @Test
        void createWithNotExistReviewerMember() {
            final Long reviewerId = -1L;
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            assertThatThrownBy(() -> reviewService.createReview(reviewee.getId(), reviewerId, reviewCreateRequest))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }

    @DisplayName("단일 리뷰 상세 정보 조회 시")
    @Nested
    class SingleReviewReadTest {

        @DisplayName("정상적으로 단일 리뷰 상세 정보를 조회한다.")
        @Test
        void validReadSingleReview() {
            final Review review = createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));

            final SingleReviewReadResponse response = reviewService.readSingleReview(review.getId());

            assertAll(
                    () -> assertThat(response.getReviewerId()).isEqualTo(review.getReviewerId()),
                    () -> assertThat(response.getTitle()).isEqualTo(review.getTitle()),
                    () -> assertThat(response.getContent()).isEqualTo(review.getContent()),
                    () -> assertThat(response.getPrUrl()).isEqualTo(review.getPrUrl()),
                    () -> assertThat(response.getStatus()).isEqualTo(review.getStatus().name())
            );
        }

        @DisplayName("리뷰 정보가 없으면 예외 발생한다.")
        @Test
        void readNotExistSingleReview() {
            final Long invalidReviewId = -1L;

            assertThatThrownBy(() -> reviewService.readSingleReview(invalidReviewId))
                    .isInstanceOf(ReviewNotFoundException.class);
        }
    }

    @DisplayName("리뷰 수정 시")
    @Nested
    class ReviewUpdateTest {

        @DisplayName("정상적으로 리뷰가 수정된다.")
        @Test
        void validUpdateReview() {
            final Review review = createReview(Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time));
            final ReviewUpdateRequest reviewUpdateRequest = new ReviewUpdateRequest("새 본문");

            reviewService.updateReview(review.getRevieweeId(), review.getId(), reviewUpdateRequest);
            entityManager.flush();
            entityManager.clear();

            final Review updatedReview = reviewRepository.findById(review.getId())
                    .orElseThrow(ReviewNotFoundException::new);
            assertThat(updatedReview.getContent()).isEqualTo(reviewUpdateRequest.getContent());
        }

        @DisplayName("리뷰 정보가 없으면 예외 발생한다.")
        @Test
        void updateWithNotExistReview() {
            final Long reviewId = -1L;
            final ReviewUpdateRequest request = new ReviewUpdateRequest("새 본문");

            assertThatThrownBy(() -> reviewService.updateReview(1L, reviewId, request))
                    .isInstanceOf(ReviewNotFoundException.class);
        }
    }

    @DisplayName("리뷰 수락 시")
    @Nested
    class ReviewAcceptTest {

        @DisplayName("정상적으로 수락된다.")
        @Test
        void validAcceptReview() {
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

            reviewService.acceptReview(reviewerMember.getId(), review.getId());
            entityManager.flush();
            entityManager.clear();

            final Review acceptedReview = reviewRepository.findById(review.getId())
                    .orElseThrow(ReviewNotFoundException::new);
            assertThat(acceptedReview.getStatus()).isEqualTo(ReviewStatus.ACCEPTED);
        }

        @DisplayName("리뷰 정보가 없으면 예외 발생한다.")
        @Test
        void acceptWithNotExistReview() {
            final Long invalidReviewId = -1L;
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );

            assertThatThrownBy(() -> reviewService.acceptReview(reviewerMember.getId(), invalidReviewId))
                    .isInstanceOf(ReviewNotFoundException.class);
        }

        @DisplayName("요청한 회원 정보가 없으면 예외 발생한다.")
        @Test
        void acceptWithNotExistMember() {
            final Long invalidMemberId = -1L;
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

            assertThatThrownBy(() -> reviewService.acceptReview(invalidMemberId, review.getId()))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }

    @DisplayName("리뷰 완료 시")
    @Nested
    class ReviewApproveTest {

        @DisplayName("정상적으로 완료된다.")
        @Test
        void validAcceptReview() {
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

            reviewService.acceptReview(reviewerMember.getId(), review.getId());
            entityManager.flush();
            entityManager.clear();

            reviewService.approveReview(reviewerMember.getId(), review.getId());

            final Review approvedReview = reviewRepository.findById(review.getId())
                    .orElseThrow(ReviewNotFoundException::new);
            assertThat(approvedReview.getStatus()).isEqualTo(ReviewStatus.APPROVED);
        }

        @DisplayName("리뷰 정보가 없으면 예외 발생한다.")
        @Test
        void acceptWithNotExistReview() {
            final Long invalidReviewId = -1L;
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );

            assertThatThrownBy(() -> reviewService.approveReview(reviewerMember.getId(), invalidReviewId))
                    .isInstanceOf(ReviewNotFoundException.class);
        }

        @DisplayName("요청한 회원 정보가 없으면 예외 발생한다.")
        @Test
        void acceptWithNotExistMember() {
            final Long invalidMemberId = -1L;
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

            assertThatThrownBy(() -> reviewService.approveReview(invalidMemberId, review.getId()))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }

    @DisplayName("리뷰 종료 시")
    @Nested
    class ReviewFinishTest {

        @DisplayName("정상적으로 종료된다.")
        @Test
        void validFinishReview() {
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

            reviewService.refuseReview(reviewerMember.getId(), review.getId());
            entityManager.flush();
            entityManager.clear();
            reviewService.finishReview(reviewerMember.getId(), review.getId());
            entityManager.flush();
            entityManager.clear();

            assertThat(reviewRepository.findById(review.getId())).isEmpty();
        }

        @DisplayName("리뷰 정보가 없으면 예외 발생한다.")
        @Test
        void refuseWithNotExistReview() {
            final Long invalidReviewId = -1L;
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );

            assertThatThrownBy(() -> reviewService.finishReview(reviewerMember.getId(), invalidReviewId))
                    .isInstanceOf(ReviewNotFoundException.class);
        }

        @DisplayName("요청한 회원 정보가 없으면 예외 발생한다.")
        @Test
        void refuseWithNotExistMember() {
            final Long invalidMemberId = -1L;
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

            assertThatThrownBy(() -> reviewService.finishReview(invalidMemberId, review.getId()))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }
}

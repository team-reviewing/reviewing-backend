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
import project.reviewing.review.application.ReviewService;
import project.reviewing.review.domain.Review;
import project.reviewing.review.exception.InvalidReviewException;
import project.reviewing.review.exception.ReviewNotFoundException;
import project.reviewing.review.presentation.request.ReviewCreateRequest;

import java.util.Set;

@DisplayName("ReviewService 는 ")
public class ReviewServiceTest extends IntegrationTest {

    @DisplayName("리뷰 생성 시 ")
    @Nested
    class ReviewCreateTest {

        @DisplayName("정상적으로 새 리뷰가 생성된다.")
        @Test
        void validCreateReview() {
            final ReviewService reviewService = new ReviewService(reviewRepository, memberRepository);
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            reviewService.createReview(reviewee.getId(), reviewerMember.getReviewer().getId(), reviewCreateRequest);

            final Review newReview = reviewRepository
                    .findByRevieweeIdAndReviewerId(reviewee.getId(), reviewerMember.getReviewer().getId())
                    .orElseThrow(ReviewNotFoundException::new);
            assertAll(
                    () -> assertThat(newReview.getTitle()).isEqualTo(reviewCreateRequest.getTitle()),
                    () -> assertThat(newReview.getContent()).isEqualTo(reviewCreateRequest.getContent()),
                    () -> assertThat(newReview.getPrUrl()).isEqualTo(reviewCreateRequest.getPrUrl())
            );
        }

        @DisplayName("동일 리뷰어에게 요청한 리뷰가 이미 존재한다면 예외 발생한다.")
        @Test
        void createAlreadyExistReviewToSameReviewer() {
            final ReviewService reviewService = new ReviewService(reviewRepository, memberRepository);
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            createReview(Review.assign(
                    reviewee.getId(), reviewerMember.getReviewer().getId(),
                    reviewCreateRequest.getTitle(), reviewCreateRequest.getContent(),
                    reviewCreateRequest.getPrUrl(), reviewerMember.isReviewer(), reviewerMember.getId()
            ));

            assertThatThrownBy(() -> reviewService.createReview(
                    reviewee.getId(), reviewerMember.getReviewer().getId(), reviewCreateRequest
                    ))
                    .isInstanceOf(InvalidReviewException.class)
                    .hasMessage(ErrorType.ALREADY_REQUESTED.getMessage());
        }

        @DisplayName("리뷰어의 회원 정보가 없으면 예외 발생한다.")
        @Test
        void createReviewWithNotExistReviewerMember() {
            final ReviewService reviewService = new ReviewService(reviewRepository, memberRepository);
            final long reviewerId = -1L;
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final ReviewCreateRequest reviewCreateRequest = new ReviewCreateRequest(
                    "리뷰 요청합니다.", "본문", "https://github.com/Tom/myproject/pull/1"
            );

            assertThatThrownBy(() -> reviewService.createReview(reviewee.getId(), reviewerId, reviewCreateRequest))
                    .isInstanceOf(MemberNotFoundException.class);
        }
    }
}

package project.reviewing.integration.review.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewStatus;
import project.reviewing.review.presentation.data.RoleInReview;
import project.reviewing.review.query.application.ReviewQueryService;
import project.reviewing.review.query.application.response.ReviewsResponse;
import project.reviewing.review.query.dao.data.ReviewByRoleData;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("ReviewQueryService는 ")
public class ReviewQueryServiceTest extends IntegrationTest {

    private ReviewQueryService reviewQueryService;

    @BeforeEach
    void setUp() {
        reviewQueryService = new ReviewQueryService(reviewsDAO);
    }

    @DisplayName("역할별 내 리뷰 목록 조회 시 ")
    @Nested
    class ReviewsByRoleFind {

        @DisplayName("리뷰어 역할로 모든 상태의 리뷰 목록을 조회한다.")
        @Test
        void validFindByReviewer() {
            // given
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewee1 = createMember(new Member(2L, "Alex", "Alex@gmail.com", "imageUrl", "https://github.com/Alex"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(3L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Review review = createReview(
                    Review.assign(
                            reviewee.getId(), reviewerMember.getReviewer().getId(),
                            "제목", "본문", "prUrl", reviewerMember.getId(), reviewerMember.isReviewer(), time
                    ));
            final Review review1 = createReview(
                    Review.assign(
                            reviewee1.getId(), reviewerMember.getReviewer().getId(),
                            "제목1", "본문1", "prUrl1", reviewerMember.getId(), reviewerMember.isReviewer(), time
                    ));
            final ReviewsResponse expectedResponse = ReviewsResponse.from(
                    List.of(toReviewByRoleData(review, reviewee), toReviewByRoleData(review1, reviewee1))
            );

            // when
            final ReviewsResponse response = reviewQueryService.findReviewsByRole(
                    reviewerMember.getId(), RoleInReview.ROLE_REVIEWER, ReviewStatus.NONE
            );

            // then
            assertAll(
                    () -> assertThat(response.getReviews()).hasSize(2),
                    () -> assertThat(response.getReviews())
                            .usingRecursiveFieldByFieldElementComparator()
                            .containsAll(expectedResponse.getReviews())
            );
        }

        @DisplayName("리뷰이 역할로 모든 상태의 리뷰 목록을 조회한다.")
        @Test
        void validFindByReviewee() {
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "Alex", "Alex@gmail.com", "imageUrl", "https://github.com/Alex"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Member reviewerMember1 = createMemberAndRegisterReviewer(
                    new Member(3L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Review review = createReview(
                    Review.assign(
                            reviewee.getId(), reviewerMember.getReviewer().getId(),
                            "제목", "본문", "prUrl", reviewerMember.getId(), reviewerMember.isReviewer(), time
                    ));
            final Review review1 = createReview(
                    Review.assign(
                            reviewee.getId(), reviewerMember1.getReviewer().getId(),
                            "제목1", "본문1", "prUrl1", reviewerMember1.getId(), reviewerMember1.isReviewer(), time
                    ));
            final ReviewsResponse expectedResponse = ReviewsResponse.from(
                    List.of(toReviewByRoleData(review, reviewerMember), toReviewByRoleData(review1, reviewerMember1))
            );

            // when
            final ReviewsResponse response = reviewQueryService.findReviewsByRole(
                    reviewee.getId(), RoleInReview.ROLE_REVIEWEE, ReviewStatus.NONE
            );

            // then
            assertAll(
                    () -> assertThat(response.getReviews()).hasSize(2),
                    () -> assertThat(response.getReviews())
                            .usingRecursiveFieldByFieldElementComparator()
                            .containsAll(expectedResponse.getReviews())
            );
        }

        @DisplayName("리뷰어 역할로 ACCEPTED 상태인 리뷰 목록을 조회한다.")
        @Test
        void validFindByReviewerWithAcceptedStatus() {
            // given
            final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
            final Member reviewee1 = createMember(new Member(2L, "Alex", "Alex@gmail.com", "imageUrl", "https://github.com/Alex"));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(3L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
            );
            final Review review = createReview(
                    Review.assign(
                            reviewee.getId(), reviewerMember.getReviewer().getId(),
                            "제목", "본문", "prUrl", reviewerMember.getId(), reviewerMember.isReviewer(), time
                    ));
            createReview(
                    Review.assign(
                            reviewee1.getId(), reviewerMember.getReviewer().getId(),
                            "제목1", "본문1", "prUrl1", reviewerMember.getId(), reviewerMember.isReviewer(), time
                    ));

            review.accept(time);
            entityManager.merge(review);
            entityManager.flush();
            entityManager.clear();

            final ReviewsResponse expectedResponse = ReviewsResponse.from(
                    List.of(toReviewByRoleData(review, reviewee))
            );

            // when
            final ReviewsResponse response = reviewQueryService.findReviewsByRole(
                    reviewerMember.getId(), RoleInReview.ROLE_REVIEWER, ReviewStatus.ACCEPTED
            );

            // then
            assertAll(
                    () -> assertThat(response.getReviews()).hasSize(1),
                    () -> assertThat(response.getReviews())
                            .usingRecursiveFieldByFieldElementComparator()
                            .containsAll(expectedResponse.getReviews())
            );
        }
    }

    private ReviewByRoleData toReviewByRoleData(final Review review, final Member member) {
        return new ReviewByRoleData(
                review.getId(), review.getTitle(), review.getReviewerId(), review.getStatus(),
                member.getId(), member.getUsername(), member.getImageUrl()
        );
    }
}

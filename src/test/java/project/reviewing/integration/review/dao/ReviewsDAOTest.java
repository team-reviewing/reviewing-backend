package project.reviewing.integration.review.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.query.dao.data.ReviewByRoleData;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ReviewsDAO는 ")
public class ReviewsDAOTest extends IntegrationTest {

    @DisplayName("리뷰어 역할로 내 리뷰 목록을 조회한다.")
    @Test
    void findReviewsByReviewer() {
        final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));
        final Member reviewee1 = createMember(new Member(2L, "Alex", "Alex@gmail.com", "imageUrl", "https://github.com/Alex"));
        final Member reviewerMember = createMemberAndRegisterReviewer(
                new Member(3L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L), "소개글")
        );
        final Review review = createReview(
                Review.assign(
                        reviewee.getId(), reviewerMember.getReviewer().getId(),
                        "제목", "본문", "prUrl", reviewerMember.getId(), reviewerMember.isReviewer()
                ));
        final Review review1 = createReview(
                Review.assign(
                        reviewee1.getId(), reviewerMember.getReviewer().getId(),
                        "제목1", "본문1", "prUrl1", reviewerMember.getId(), reviewerMember.isReviewer()
                ));

        List<ReviewByRoleData> reviewByRoleDataList = reviewsDAO.findReviewsByReviewer(reviewerMember.getId());

        assertThat(reviewByRoleDataList)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(toReviewByRoleData(review, reviewee), toReviewByRoleData(review1, reviewee1));
    }

    @DisplayName("리뷰이 역할로 내 리뷰 목록을 조회한다.")
    @Test
    void findReviewsByReviewee() {
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
                        "제목", "본문", "prUrl", reviewerMember.getId(), reviewerMember.isReviewer()
                ));
        final Review review1 = createReview(
                Review.assign(
                        reviewee.getId(), reviewerMember1.getReviewer().getId(),
                        "제목1", "본문1", "prUrl1", reviewerMember1.getId(), reviewerMember1.isReviewer()
                ));

        List<ReviewByRoleData> reviewByRoleDataList = reviewsDAO.findReviewsByReviewee(reviewee.getId());

        assertThat(reviewByRoleDataList)
                .usingRecursiveFieldByFieldElementComparator()
                .contains(toReviewByRoleData(review, reviewerMember), toReviewByRoleData(review1, reviewerMember1));
    }

    @DisplayName("리뷰 정보가 없으면 빈 리스트를 반환한다.")
    @Test
    void findNotExistReviews() {
        final Member reviewee = createMember(new Member(1L, "Tom", "Tom@gmail.com", "imageUrl", "https://github.com/Tom"));

        assertThat(reviewsDAO.findReviewsByReviewee(reviewee.getId()))
                .isEmpty();
    }

    private ReviewByRoleData toReviewByRoleData(final Review review, final Member member) {
        return new ReviewByRoleData(
                review.getId(), review.getTitle(), review.getReviewerId(),
                member.getId(), member.getUsername(), member.getImageUrl()
        );
    }
}

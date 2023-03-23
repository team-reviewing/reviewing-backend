package project.reviewing.unit.review.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.review.domain.Review;
import project.reviewing.review.exception.InvalidReviewException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Review 는 ")
public class ReviewTest {

    @DisplayName("리뷰를 생성할 수 있다.")
    @Test
    void validCreateReview() {
        final Review newReview = Review.assign(1L, 2L, "제목", "본문", "github.com/bboor/project/pull/1", 2L, true);

        assertAll(
                () -> assertThat(newReview.getRevieweeId()).isEqualTo(1L),
                () -> assertThat(newReview.getReviewerId()).isEqualTo(2L)
        );
    }

    @DisplayName("리뷰이와 동일한 리뷰어에게 요청하는 리뷰는 생성할 수 없다.")
    @Test
    void createWithSameReviewerAsReviewee() {
        final Long revieweeId = 1L;
        final Long reviewerMemberId = 1L;

        assertThatThrownBy(
                () -> Review.assign(
                        revieweeId, 1L, "제목", "본문", "github.com/bboor/project/pull/1", reviewerMemberId, true
                ))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.SAME_REVIEWER_AS_REVIEWEE.getMessage());
    }

    @DisplayName("활동하지 않는 리뷰어에게 요청하는 리뷰는 생성할 수 없다.")
    @Test
    void createWithNotRegisteredReviewer() {
        final boolean isReviewer = false;

        assertThatThrownBy(() -> Review.assign(1L, 2L, "제목", "본문", "github.com/bboor/project/pull/1", 2L, isReviewer))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.DO_NOT_REGISTERED.getMessage());
    }

    @DisplayName("리뷰를 수정할 수 있다.")
    @Test
    void validUpdateReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "github.com/bboor/project/pull/1", 2L, true);

        review.update(1L, "수정본문");

        assertThat(review.getContent()).isEqualTo("수정본문");
    }

    @DisplayName("리뷰를 생성한 리뷰이가 아니면 수정할 수 없다.")
    @Test
    void updateWithNotRevieweeOfReview() {
        final Long revieweeId = 1L;
        final Long anotherRevieweeId = 2L;
        final Review review = Review.assign(revieweeId, 1L, "제목", "본문", "github.com/bboor/project/pull/1", 2L, true);

        assertThatThrownBy(() -> review.update(anotherRevieweeId, "수정본문"))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.NOT_REVIEWEE_OF_REVIEW.getMessage());
    }
}

package project.reviewing.unit.review.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.common.util.ReviewingTime;
import project.reviewing.common.util.Time;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.command.domain.ReviewStatus;
import project.reviewing.review.exception.InvalidReviewException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Review 는 ")
public class ReviewTest {

    private Time time;

    @BeforeEach
    void setUp() {
        time = new ReviewingTime();
    }

    @DisplayName("리뷰를 생성할 수 있다.")
    @Test
    void validCreateReview() {
        assertDoesNotThrow(() -> Review.assign(1L, 2L, "제목", "본문", "github.com/bboor/project/pull/1", 2L, true, time));
    }

    @DisplayName("리뷰이와 동일한 리뷰어에게 요청하는 리뷰는 생성할 수 없다.")
    @Test
    void createWithSameReviewerAsReviewee() {
        assertThatThrownBy(() -> Review.assign(1L, 1L, "제목", "본문", "github.com/bboor/project/pull/1", 1L, true, time))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.SAME_REVIEWER_AS_REVIEWEE.getMessage());
    }

    @DisplayName("활동하지 않는 리뷰어에게 요청하는 리뷰는 생성할 수 없다.")
    @Test
    void createWithNotRegisteredReviewer() {
        assertThatThrownBy(() -> Review.assign(1L, 2L, "제목", "본문", "github.com/bboor/project/pull/1", 2L, false, time))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.REVIEWER_NOT_ACTIVE.getMessage());
    }

    @DisplayName("리뷰를 수정할 수 있다.")
    @Test
    void validUpdateReview() {
        final String updatingContent = "새 본문";
        final Review review = Review.assign(1L, 2L, "제목", "본문", "github.com/bboor/project/pull/1", 2L, true, time);

        review.update(1L, updatingContent);

        assertThat(review.getContent()).isEqualTo(updatingContent);
    }

    @DisplayName("리뷰를 생성한 리뷰이가 아니면 수정할 수 없다.")
    @Test
    void updateWithNotRevieweeOfReview() {
        final Long revieweeId = 1L;
        final Long invalidRevieweeId = 2L;
        final Review review = Review.assign(revieweeId, 2L, "제목", "본문", "github.com/bboor/project/pull/1", 2L, true, time);

        assertThatThrownBy(() -> review.update(invalidRevieweeId, "새 본문"))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.NOT_REVIEWEE_OF_REVIEW.getMessage());
    }

    @DisplayName("리뷰를 수락할 수 있다.")
    @Test
    void validAcceptReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        review.accept(time);

        assertThat(review.getStatus()).isEqualTo(ReviewStatus.ACCEPTED);
    }

    @DisplayName("리뷰를 수락할 수 있는지 조건을 확인할 수 있다.")
    @Test
    void validCheckAcceptReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        assertThat(review.canAccept(1L)).isTrue();
    }

    @DisplayName("리뷰를 요청받은 리뷰어가 아니면 수락할 수 없다.")
    @Test
    void acceptWithNotReviewerOfReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        assertThatThrownBy(() -> review.canAccept(2L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.NOT_REVIEWER_OF_REVIEW.getMessage());
    }

    @DisplayName("리뷰의 상태가 CREATED(생성) 상태가 아니면 수락할 수 없다.")
    @Test
    void acceptWithNotProperStatus() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        review.accept(time);

        assertThatThrownBy(() -> review.canAccept(1L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.NOT_PROPER_REVIEW_STATUS.getMessage());
    }

    @DisplayName("리뷰를 거절할 수 있다.")
    @Test
    void validRefuseReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        review.refuse(time);

        assertThat(review.getStatus()).isEqualTo(ReviewStatus.REFUSED);
    }

    @DisplayName("리뷰를 거절할 수 있는지 조건을 확인할 수 있다.")
    @Test
    void validCheckRefuseReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        assertThat(review.canRefuse(1L)).isTrue();
    }

    @DisplayName("리뷰를 요청받은 리뷰어가 아니면 거절할 수 없다.")
    @Test
    void refuseWithNotReviewerOfReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        assertThatThrownBy(() -> review.canRefuse(2L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.NOT_REVIEWER_OF_REVIEW.getMessage());
    }

    @DisplayName("리뷰의 상태가 CREATED(생성) 상태가 아니면 거절할 수 없다.")
    @Test
    void refuseWithNotProperStatus() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        review.accept(time);

        assertThatThrownBy(() -> review.canRefuse(1L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.NOT_PROPER_REVIEW_STATUS.getMessage());
    }

    @DisplayName("리뷰를 완료할 수 있다.")
    @Test
    void validApproveReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        review.approve(time);

        assertThat(review.getStatus()).isEqualTo(ReviewStatus.APPROVED);
    }

    @DisplayName("리뷰를 완료할 수 있는지 조건을 확인할 수 있다.")
    @Test
    void validCheckApproveReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        review.accept(time);

        assertThat(review.canApprove(1L)).isTrue();
    }

    @DisplayName("리뷰를 요청받은 리뷰어가 아니면 완료할 수 없다.")
    @Test
    void approveWithNotReviewerOfReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        assertThatThrownBy(() -> review.canAccept(2L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.NOT_REVIEWER_OF_REVIEW.getMessage());
    }

    @DisplayName("리뷰의 상태가 ACCEPTED(수락) 상태가 아니면 완료할 수 없다.")
    @Test
    void approveWithNotProperStatus() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        assertThatThrownBy(() -> review.canApprove(1L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.NOT_PROPER_REVIEW_STATUS.getMessage());
    }

    @DisplayName("리뷰를 종료할 수 있는지 조건을 확인할 수 있다.")
    @Test
    void validCloseReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        assertThat(review.canClose(1L, -1L)).isTrue();

        review.refuse(time);

        assertThat(review.canClose(-1L, 1L)).isTrue();
    }

    @DisplayName("리뷰를 요청받은 리뷰어나 요청한 리뷰이가 아니면 종료할 수 없다.")
    @Test
    void closeWithNotReviewerOfReview() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        assertThatThrownBy(() -> review.canClose(-1L, -1L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.ROLE_IN_REVIEW_NOT_FOUND.getMessage());
    }

    @DisplayName("리뷰어의 입장에서 리뷰의 상태가 REFUSED(거절) 상태가 아니면 종료할 수 없다.")
    @Test
    void closeWithNotRefusedStatus() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        assertThatThrownBy(() -> review.canClose(-1L, 1L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.NOT_PROPER_REVIEW_STATUS.getMessage());
    }

    @DisplayName("리뷰이의 입장에서 리뷰의 상태가 CREATED(생성) 상태가 아니면 종료할 수 없다.")
    @Test
    void closeWithNotCreatedStatus() {
        final Review review = Review.assign(1L, 1L, "제목", "본문", "prUrl", 2L, true, time);

        review.accept(time);

        assertThatThrownBy(() -> review.canClose(1L, -1L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.NOT_PROPER_REVIEW_STATUS.getMessage());
    }
}

package project.reviewing.unit.review.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.review.domain.Review;
import project.reviewing.review.exception.InvalidReviewException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Review 는 ")
public class ReviewTest {

    @DisplayName("리뷰이와 리뷰어를 지정할 수 있다.")
    @CsvSource(value = {"1, 2, true"})
    @ParameterizedTest
    void validAssign(final Long revieweeId, final Long reviewerId, final Boolean isReviewer) {
        final Review newReview = new Review(null, null, "제목", "본문", "github.com/bboor/project/pull/1");

        newReview.assign(revieweeId, reviewerId, isReviewer);

        assertAll(
                () -> assertThat(newReview.getRevieweeId()).isEqualTo(revieweeId),
                () -> assertThat(newReview.getReviewerId()).isEqualTo(reviewerId)
        );
    }

    @DisplayName("리뷰이와 동일한 리뷰어는 지정할 수 없다.")
    @CsvSource(value = {"1, 1, true"})
    @ParameterizedTest
    void assignWithSameReviewerAsReviewee(final Long revieweeId, final Long reviewerId, final Boolean isReviewer) {
        final Review review = new Review(null, null, "제목", "본문", "github.com/bboor/project/pull/1");

        assertThatThrownBy(() -> review.assign(revieweeId, reviewerId, isReviewer))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.SAME_REVIEWER_AS_REVIEWEE.getMessage());
    }

    @DisplayName("활동하지 않는 리뷰어는 지정할 수 없다.")
    @CsvSource(value = {"1, 2, false"})
    @ParameterizedTest
    void assignWithNotRegisteredReviewer(final Long revieweeId, final Long reviewerId, final Boolean isReviewer) {
        final Review review = new Review(null, null, "제목", "본문", "github.com/bboor/project/pull/1");

        assertThatThrownBy(() -> review.assign(revieweeId, reviewerId, isReviewer))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.DO_NOT_REGISTERED.getMessage());
    }
}

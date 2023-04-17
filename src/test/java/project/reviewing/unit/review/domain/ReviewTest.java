package project.reviewing.unit.review.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.review.domain.Review;
import project.reviewing.review.exception.InvalidReviewException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Review 는 ")
public class ReviewTest {

    @DisplayName("리뷰를 생성할 수 있다.")
    @Test
    void validCreateReview() {
        assertDoesNotThrow(() -> Review.assign(1L, 2L, "제목", "본문", "github.com/bboor/project/pull/1", true, 2L));
    }

    @DisplayName("리뷰이와 동일한 리뷰어에게 요청하는 리뷰는 생성할 수 없다.")
    @Test
    void createWithSameReviewerAsReviewee() {
        assertThatThrownBy(() -> Review.assign(1L, 1L, "제목", "본문", "github.com/bboor/project/pull/1", true, 1L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.SAME_REVIEWER_AS_REVIEWEE.getMessage());
    }

    @DisplayName("활동하지 않는 리뷰어에게 요청하는 리뷰는 생성할 수 없다.")
    @Test
    void createWithNotRegisteredReviewer() {
        assertThatThrownBy(() -> Review.assign(1L, 2L, "제목", "본문", "github.com/bboor/project/pull/1", false, 2L))
                .isInstanceOf(InvalidReviewException.class)
                .hasMessage(ErrorType.DO_NOT_REGISTERED.getMessage());
    }
}

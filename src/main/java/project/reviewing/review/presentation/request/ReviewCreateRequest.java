package project.reviewing.review.presentation.request;

import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
public class ReviewCreateRequest {

    @Min(value = 1, message = "리뷰어의 id를 입력해 주세요.")
    private final long reviewerMemberId;

    @Size(min = 1, max = 50, message = "제목은 50자 이하로 작성해 주세요.")
    @NotEmpty(message = "제목을 입력해 주세요.")
    private final String title;

    @Size(min = 1, max = 500, message = "본문은 500자 이하로 작성해 주세요.")
    @NotEmpty(message = "본문을 입력해 주세요.")
    private final String text;

    @NotBlank(message = "PR url을 입력해 주세요.")
    private final String prUrl;

    public ReviewCreateRequest(final long reviewerMemberId, final String title, final String text, final String prUrl) {
        this.reviewerMemberId = reviewerMemberId;
        this.title = title;
        this.text = text;
        this.prUrl = prUrl;
    }
}

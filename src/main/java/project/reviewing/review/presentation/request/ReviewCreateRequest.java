package project.reviewing.review.presentation.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
public class ReviewCreateRequest {

    @Size(min = 1, max = 50, message = "제목은 50자 이하로 작성해 주세요.")
    @NotEmpty(message = "제목을 입력해 주세요.")
    private final String title;

    @Size(min = 1, max = 1500, message = "본문은 1500자 이하로 작성해 주세요.")
    @NotEmpty(message = "본문을 입력해 주세요.")
    private final String content;

    @NotBlank(message = "PR url을 입력해 주세요.")
    private final String prUrl;

    public ReviewCreateRequest(final String title, final String content, final String prUrl) {
        this.title = title;
        this.content = content;
        this.prUrl = prUrl;
    }
}

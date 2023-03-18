package project.reviewing.review.presentation.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
public class ReviewCreateRequest {

    @Size(min = 1, max = 50, message = "제목은 50자 이하로 작성해 주세요.")
    @NotBlank(message = "제목을 입력해 주세요.")
    private final String title;

    @Size(min = 1, max = 1500, message = "본문은 1500자 이하로 작성해 주세요.")
    @NotBlank(message = "본문을 입력해 주세요.")
    private final String content;

    @Pattern(regexp = "^(https://)?github\\.com/.+/.+/pull/[0-9]+$", message = "올바른 PR URL을 입력해 주세요.")
    @NotBlank(message = "PR URL을 입력해 주세요.")
    private final String prUrl;

    public ReviewCreateRequest(final String title, final String content, final String prUrl) {
        this.title = title;
        this.content = content;
        this.prUrl = prUrl;
    }
}

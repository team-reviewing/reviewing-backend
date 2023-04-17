package project.reviewing.review.presentation.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ReviewUpdateRequest {

    @Size(min = 1, max = 1500, message = "본문은 1500자 이하로 작성해 주세요.")
    @NotBlank
    private String content;

    public ReviewUpdateRequest(final String content) {
        this.content = content;
    }
}

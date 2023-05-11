package project.reviewing.evaluation.presentation.request;

import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class EvaluationCreateRequest {

    @NotNull
    private final Long reviewId;

    @Min(0) @Max(5)
    @NotNull
    private final Float score;

    @Size(min = 1, max = 100, message = "본문은 100자 이하로 작성해 주세요.")
    @NotBlank(message = "본문을 입력해 주세요.")
    private final String content;

    public EvaluationCreateRequest(final Long reviewId, final Float score, final String content) {
        this.reviewId = reviewId;
        this.score = score;
        this.content = content;
    }
}

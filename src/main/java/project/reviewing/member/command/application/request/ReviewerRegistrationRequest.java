package project.reviewing.member.command.application.request;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewerRegistrationRequest {

    @NotBlank(message = "직무를 입력해 주세요.")
    private String job;

    @NotBlank(message = "경력을 입력해 주세요.")
    private String career;

    @NotNull(message = "기술 스택을 입력해 주세요.")
    @Size(min = 1, message = "기술 스택을 입력해 주세요.")
    private List<Long> techStack;

    private String introduction;

    public ReviewerRegistrationRequest(
            final String job, final String career, final List<Long> techStack, final String introduction
    ) {
        this.job = job;
        this.career = career;
        this.techStack = techStack;
        this.introduction = introduction;
    }
}

package project.reviewing.member.command.application.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.reviewing.member.command.domain.Member;

@Getter
@NoArgsConstructor
public class MyInformationUpdateRequest {

    @NotBlank(message = "username을 입력해 주세요.")
    private String username;

    @NotBlank(message = "email을 입력해 주세요.")
    private String email;

    public MyInformationUpdateRequest(final String username, final String email) {
        this.username = username;
        this.email = email;
    }

    public Member toEntity() {
        return new Member(null, username, email, null, null);
    }
}

package project.reviewing.member.application.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdatingMemberRequest {

    @NotBlank(message = "username을 입력해 주세요.")
    private String username;

    @NotBlank(message = "email을 입력해 주세요.")
    private String email;

    public UpdatingMemberRequest(final String username, final String email) {
        this.username = username;
        this.email = email;
    }
}

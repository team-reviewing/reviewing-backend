package project.reviewing.auth.application.response;

import lombok.Getter;

@Getter
public class LoginResponse {

    private Long memberId;
    private final String accessToken;
    private final String refreshToken;
    private final boolean isCreated;

    public LoginResponse(final Long memberId, final String accessToken,
                         final String refreshToken, final boolean isCreated) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isCreated = isCreated;
    }
}

package project.reviewing.member.application.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.reviewing.member.domain.Member;

@Getter
@NoArgsConstructor
public class MyInformationResponse {

    private String username;
    private String email;
    private String imageUrl;
    private String profileUrl;
    private boolean isReviewer;

    public static MyInformationResponse of(final Member member) {
        return new MyInformationResponse(
                member.getUsername(), member.getEmail(),
                member.getImageUrl(), member.getProfileUrl(),
                member.isReviewer()
        );
    }

    private MyInformationResponse(
            final String username, final String email,
            final String imageUrl, final String profileUrl,
            final boolean isReviewer
    ) {
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.profileUrl = profileUrl;
        this.isReviewer = isReviewer;
    }
}

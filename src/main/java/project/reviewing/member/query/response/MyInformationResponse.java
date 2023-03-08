package project.reviewing.member.query.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import project.reviewing.member.query.dao.MyInformation;

@Getter
@NoArgsConstructor
public class MyInformationResponse {

    private String username;
    private String email;
    private String imageUrl;
    private String profileUrl;
    private boolean isReviewer;

    public static MyInformationResponse of(final MyInformation myInformation) {
        return new MyInformationResponse(
                myInformation.getUsername(), myInformation.getEmail(),
                myInformation.getImageUrl(), myInformation.getProfileUrl(),
                myInformation.isReviewer()
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

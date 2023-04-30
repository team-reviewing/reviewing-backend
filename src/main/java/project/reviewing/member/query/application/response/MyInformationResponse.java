package project.reviewing.member.query.application.response;

import lombok.Getter;
import project.reviewing.member.query.dao.data.MyInformationData;

@Getter
public class MyInformationResponse {

    private final String username;
    private final String email;
    private final String imageUrl;
    private final String profileUrl;
    private final Boolean isReviewer;

    public static MyInformationResponse of(final MyInformationData myInformationData) {
        return new MyInformationResponse(
                myInformationData.getUsername(), myInformationData.getEmail(),
                myInformationData.getImageUrl(), myInformationData.getProfileUrl(),
                myInformationData.isReviewer()
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

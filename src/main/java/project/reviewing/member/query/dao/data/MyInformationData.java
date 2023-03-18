package project.reviewing.member.query.dao.data;

import lombok.Getter;

@Getter
public class MyInformationData {

    private final String username;
    private final String email;
    private final String imageUrl;
    private final String profileUrl;
    private final boolean isReviewer;

    public MyInformationData(
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

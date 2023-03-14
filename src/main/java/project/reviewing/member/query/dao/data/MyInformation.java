package project.reviewing.member.query.dao.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MyInformation {

    private String username;
    private String email;
    private String imageUrl;
    private String profileUrl;
    private boolean isReviewer;

    public MyInformation(final String username, final String email, final String imageUrl, final String profileUrl,
                         final boolean isReviewer) {
        this.username = username;
        this.email = email;
        this.imageUrl = imageUrl;
        this.profileUrl = profileUrl;
        this.isReviewer = isReviewer;
    }
}

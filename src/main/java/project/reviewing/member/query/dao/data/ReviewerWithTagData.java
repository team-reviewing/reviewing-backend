package project.reviewing.member.query.dao.data;

import lombok.Getter;
import project.reviewing.tag.query.dao.data.TagData;

@Getter
public class ReviewerWithTagData {

    private final Long id;
    private final String job;
    private final String career;
    private final String introduction;
    private final String username;
    private final String imageUrl;
    private final String profileUrl;
    private final TagData tagData;

    public ReviewerWithTagData(
            final Long id, final String job,
            final String career, final String introduction,
            final String username, final String imageUrl,
            final String profileUrl, final TagData tagData
    ) {
        this.id = id;
        this.job = job;
        this.career = career;
        this.introduction = introduction;
        this.username = username;
        this.imageUrl = imageUrl;
        this.profileUrl = profileUrl;
        this.tagData = tagData;
    }
}

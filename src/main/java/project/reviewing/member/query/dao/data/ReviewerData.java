package project.reviewing.member.query.dao.data;

import java.util.List;
import lombok.Getter;
import project.reviewing.tag.query.dao.data.TagData;

@Getter
public class ReviewerData {

    private final Long id;
    private final String job;
    private final String career;
    private final String introduction;
    private final String username;
    private final String imageUrl;
    private final String profileUrl;
    private final List<TagData> tagData;
    private final Float score;

    public ReviewerData(
            final Long id, final String job, final String career,
            final String introduction, final String username, final String imageUrl,
            final String profileUrl, final List<TagData> tagData, final Float score
    ) {
        this.id = id;
        this.job = job;
        this.career = career;
        this.introduction = introduction;
        this.username = username;
        this.imageUrl = imageUrl;
        this.profileUrl = profileUrl;
        this.tagData = tagData;
        this.score = score;
    }
}

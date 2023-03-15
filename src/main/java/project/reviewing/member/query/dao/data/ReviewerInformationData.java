package project.reviewing.member.query.dao.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.reviewing.tag.query.dao.data.TagData;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewerInformationData {

    private String job;
    private String career;
    private String introduction;
    private TagData tagData;

    public ReviewerInformationData(final String job, final String career, final String introduction, final TagData tagData) {
        this.job = job;
        this.career = career;
        this.introduction = introduction;
        this.tagData = tagData;
    }
}

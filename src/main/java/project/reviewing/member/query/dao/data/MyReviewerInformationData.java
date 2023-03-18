package project.reviewing.member.query.dao.data;

import lombok.Getter;
import project.reviewing.tag.query.dao.data.TagData;

@Getter
public class MyReviewerInformationData {

    private final String job;
    private final String career;
    private final String introduction;
    private final TagData tagData;

    public MyReviewerInformationData(
            final String job, final String career, final String introduction, final TagData tagData
    ) {
        this.job = job;
        this.career = career;
        this.introduction = introduction;
        this.tagData = tagData;
    }
}

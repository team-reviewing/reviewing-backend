package project.reviewing.member.query.dao.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewerData {

    private String job;
    private String career;
    private String introduction;

    public ReviewerData(final String job, final String career, final String introduction) {
        this.job = job;
        this.career = career;
        this.introduction = introduction;
    }
}

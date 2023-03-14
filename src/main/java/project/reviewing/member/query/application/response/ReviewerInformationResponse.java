package project.reviewing.member.query.application.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.reviewing.member.query.dao.data.ReviewerData;
import project.reviewing.tag.command.application.response.TagResponse;
import project.reviewing.tag.query.dao.TagData;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewerInformationResponse {

    private String job;
    private String career;
    private List<TagData> techStack;
    private String introduction;
    private List<String> jobList;
    private List<String> careerList;
    private List<TagResponse> tagList;

    public static ReviewerInformationResponse from(final ReviewerData reviewerData, final List<TagData> techStack) {
        return new ReviewerInformationResponse(
                reviewerData.getJob(), reviewerData.getCareer(), techStack, reviewerData.getIntroduction()
        );
    }

    public static ReviewerInformationResponse empty() {
        return new ReviewerInformationResponse(null, null, List.of(), null);
    }

    public void addChoiceList(final List<String> jobList, final List<String> careerList, final List<TagResponse> tagList) {
        this.jobList = jobList;
        this.careerList = careerList;
        this.tagList = tagList;
    }

    private ReviewerInformationResponse(
            final String job, final String career, final List<TagData> techStack, final String introduction) {
        this.job = job;
        this.career = career;
        this.techStack = techStack;
        this.introduction = introduction;
    }
}

package project.reviewing.member.query.application.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.reviewing.member.query.dao.data.ReviewerInformationData;
import project.reviewing.tag.query.application.response.TagResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewerInformationResponse {

    private String job;
    private String career;
    private List<TagResponse> techStack;
    private String introduction;
    private List<String> jobList;
    private List<String> careerList;
    private List<TagResponse> tagList;

    public static ReviewerInformationResponse of(
            final List<ReviewerInformationData> reviewerInformationData,
            final List<String> jobList,
            final List<String> careerList,
            final List<TagResponse> tagList
    ) {
        final List<TagResponse> techStack = reviewerInformationData.stream()
                .map(data -> TagResponse.from(data.getTagData()))
                .collect(Collectors.toList());

        return new ReviewerInformationResponse(
                reviewerInformationData.get(0).getJob(),
                reviewerInformationData.get(0).getCareer(),
                techStack,
                reviewerInformationData.get(0).getIntroduction(),
                jobList,
                careerList,
                tagList
        );
    }

    public static ReviewerInformationResponse empty() {
        return new ReviewerInformationResponse(null, null, List.of(), null, List.of(), List.of(), List.of());
    }

    private ReviewerInformationResponse(
            final String job, final String career, final List<TagResponse> techStack, final String introduction,
            final List<String> jobList, final List<String> careerList, final List<TagResponse> tagList
    ) {
        this.job = job;
        this.career = career;
        this.techStack = techStack;
        this.introduction = introduction;
        this.jobList = jobList;
        this.careerList = careerList;
        this.tagList = tagList;
    }
}

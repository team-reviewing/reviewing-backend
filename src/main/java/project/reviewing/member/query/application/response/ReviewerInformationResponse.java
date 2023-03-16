package project.reviewing.member.query.application.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import project.reviewing.member.query.dao.data.ReviewerInformationData;
import project.reviewing.tag.query.application.response.TagResponse;

@Getter
public class ReviewerInformationResponse {

    private final String job;
    private final String career;
    private final List<TagResponse> techStack;
    private final String introduction;
    private final List<String> jobList;
    private final List<String> careerList;
    private final List<TagResponse> tagList;

    public static ReviewerInformationResponse of(
            final List<ReviewerInformationData> reviewerInformationData,
            final List<String> jobList,
            final List<String> careerList,
            final List<TagResponse> tagList
    ) {
        return new ReviewerInformationResponse(
                reviewerInformationData.get(0).getJob(),
                reviewerInformationData.get(0).getCareer(),
                mapTagResponse(reviewerInformationData),
                reviewerInformationData.get(0).getIntroduction(),
                jobList,
                careerList,
                tagList
        );
    }

    public static ReviewerInformationResponse empty() {
        return new ReviewerInformationResponse(null, null, List.of(), null, List.of(), List.of(), List.of());
    }

    private static List<TagResponse> mapTagResponse(final List<ReviewerInformationData> reviewerInformationData) {
        return reviewerInformationData.stream()
                .map(data -> TagResponse.from(data.getTagData()))
                .collect(Collectors.toList());
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

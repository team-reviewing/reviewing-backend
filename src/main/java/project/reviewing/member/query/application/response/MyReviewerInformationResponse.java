package project.reviewing.member.query.application.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import project.reviewing.member.query.dao.data.MyReviewerInformationData;
import project.reviewing.tag.query.application.response.TagResponse;

@Getter
public class MyReviewerInformationResponse {

    private final String job;
    private final String career;
    private final List<TagResponse> techStack;
    private final String introduction;
    private final List<String> jobList;
    private final List<String> careerList;
    private final List<TagResponse> tagList;

    public static MyReviewerInformationResponse of(
            final List<MyReviewerInformationData> myReviewerInformationData,
            final List<String> jobList,
            final List<String> careerList,
            final List<TagResponse> tagList
    ) {
        return new MyReviewerInformationResponse(
                myReviewerInformationData.get(0).getJob(),
                myReviewerInformationData.get(0).getCareer(),
                mapTagResponse(myReviewerInformationData),
                myReviewerInformationData.get(0).getIntroduction(),
                jobList,
                careerList,
                tagList
        );
    }

    public static MyReviewerInformationResponse empty(
            final List<String> jobList,
            final List<String> careerList,
            final List<TagResponse> tagList
    ) {
        return new MyReviewerInformationResponse(null, null, List.of(), null, jobList, careerList, tagList);
    }

    private static List<TagResponse> mapTagResponse(final List<MyReviewerInformationData> myReviewerInformationData) {
        return myReviewerInformationData.stream()
                .map(data -> TagResponse.from(data.getTagData()))
                .collect(Collectors.toList());
    }

    private MyReviewerInformationResponse(
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

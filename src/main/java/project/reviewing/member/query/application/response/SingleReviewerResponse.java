package project.reviewing.member.query.application.response;

import lombok.Getter;
import project.reviewing.member.query.dao.data.ReviewerData;
import project.reviewing.tag.query.application.response.TagResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SingleReviewerResponse {

    private final Long id;
    private final String username;
    private final String imageUrl;
    private final String profileUrl;
    private final String job;
    private final String career;
    private final List<TagResponse> techStack;
    private final String introduction;
    private final Float score;

    public static SingleReviewerResponse from(final ReviewerData reviewerData) {
        return new SingleReviewerResponse(
                reviewerData.getId(), reviewerData.getUsername(),
                reviewerData.getImageUrl(), reviewerData.getProfileUrl(),
                reviewerData.getJob(), reviewerData.getCareer(),
                mapTagResponse(reviewerData), reviewerData.getIntroduction(),
                reviewerData.getScore()
        );
    }

    private static List<TagResponse> mapTagResponse(final ReviewerData reviewerData) {
        return reviewerData.getTagData()
                .stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }

    public SingleReviewerResponse(
            final Long id, final String username, final String imageUrl, final String profileUrl, final String job,
            final String career, final List<TagResponse> techStack, final String introduction, final Float score
    ) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.profileUrl = profileUrl;
        this.job = job;
        this.career = career;
        this.techStack = techStack;
        this.introduction = introduction;
        this.score = score;
    }
}

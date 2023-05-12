package project.reviewing.member.query.application.response;

import project.reviewing.tag.query.application.response.TagResponse;

import java.util.List;

public class SingleReviewerResponse {

    private Long id;
    private String username;
    private String imageUrl;
    private String profileUrl;
    private String job;
    private String career;
    private List<TagResponse> techStack;
    private String introduction;

    public SingleReviewerResponse() {}
}

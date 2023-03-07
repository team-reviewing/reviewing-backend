package project.reviewing.member.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MemberResponse {

    @JsonProperty(value = "image_url")
    private final String imageURL;

    public MemberResponse(final String imageURL) {
        this.imageURL = imageURL;
    }

}

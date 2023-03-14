package project.reviewing.tag.command.application.response;

import lombok.Getter;
import project.reviewing.tag.command.domain.Tag;

@Getter
public class TagResponse {

    private final Long id;
    private final String name;

    public static TagResponse from(final Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }

    private TagResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }
}

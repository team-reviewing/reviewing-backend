package project.reviewing.tag.query.application.response;

import lombok.Getter;
import project.reviewing.tag.command.domain.Tag;
import project.reviewing.tag.query.dao.data.TagData;

@Getter
public class TagResponse {

    private final Long id;
    private final String name;

    public static TagResponse from(final TagData tagData) {
        return new TagResponse(tagData.getId(), tagData.getName());
    }

    public static TagResponse from(final Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }

    private TagResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }
}

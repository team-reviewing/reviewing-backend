package project.reviewing.tag.query.dao;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagData {

    private Long id;
    private String name;

    public TagData(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }
}

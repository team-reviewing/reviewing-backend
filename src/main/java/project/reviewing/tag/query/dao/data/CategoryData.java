package project.reviewing.tag.query.dao.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class CategoryData {

    private final Long id;
    private final String name;

    public CategoryData(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }
}

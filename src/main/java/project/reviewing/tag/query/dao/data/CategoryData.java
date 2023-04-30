package project.reviewing.tag.query.dao.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class CategoryData implements Comparable<CategoryData> {

    private final Long id;
    private final String name;

    public CategoryData(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int compareTo(CategoryData o) {
        if (id - o.id < 0) {
            return -1;
        } else if (id.equals(o.id)) {
            return 0;
        }
        return 1;
    }
}

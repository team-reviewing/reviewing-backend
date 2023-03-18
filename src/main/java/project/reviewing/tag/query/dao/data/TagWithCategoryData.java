package project.reviewing.tag.query.dao.data;

import lombok.Getter;

@Getter
public class TagWithCategoryData {

    private final Long categoryId;
    private final String categoryName;
    private final Long tagId;
    private final String tagName;

    public TagWithCategoryData(final Long categoryId, final String categoryName, final Long tagId,
                               final String tagName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.tagId = tagId;
        this.tagName = tagName;
    }
}

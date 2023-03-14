package project.reviewing.tag.query.application.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import project.reviewing.tag.query.dao.data.CategoryData;
import project.reviewing.tag.query.dao.data.TagData;

@Getter
public class CategoryResponse {

    private final Long id;
    private final String name;
    private final List<TagResponse> tags;

    public static CategoryResponse of(final CategoryData categoryData, final List<TagData> tagData) {
        final List<TagResponse> tags = tagData.stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());

        return new CategoryResponse(categoryData.getId(), categoryData.getName(), tags);
    }

    private CategoryResponse(final Long id, final String name, final List<TagResponse> tags) {
        this.id = id;
        this.name = name;
        this.tags = tags;
    }
}

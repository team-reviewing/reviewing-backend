package project.reviewing.tag.query.application.response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import project.reviewing.tag.query.application.util.CategoryDataMapper;
import project.reviewing.tag.query.dao.data.CategoryData;
import project.reviewing.tag.query.dao.data.TagData;
import project.reviewing.tag.query.dao.data.TagWithCategoryData;

@Getter
public class CategoriesResponse {

    private final List<CategoryResponse> categories;

    public static CategoriesResponse from(final List<TagWithCategoryData> tagWithCategoryData) {
        final Map<CategoryData, List<TagData>> map = CategoryDataMapper.map(tagWithCategoryData);
        final List<CategoryResponse> categories = map.entrySet().stream()
                .map(entry -> CategoryResponse.of(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new CategoriesResponse(categories);
    }

    private CategoriesResponse(final List<CategoryResponse> categories) {
        this.categories = categories;
    }
}

package project.reviewing.tag.query.application.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import project.reviewing.tag.query.dao.data.CategoryData;
import project.reviewing.tag.query.dao.data.TagData;
import project.reviewing.tag.query.dao.data.TagWithCategoryData;

public class CategoryDataMapper {

    public static Map<CategoryData, List<TagData>> map(final List<TagWithCategoryData> tagWithCategoryData) {
        final Map<CategoryData, List<TagData>> map = new HashMap<>();

        for (final TagWithCategoryData data : tagWithCategoryData) {
            final CategoryData categoryData = new CategoryData(data.getCategoryId(), data.getCategoryName());
            final TagData tagData = new TagData(data.getTagId(), data.getTagName());

            if (!map.containsKey(categoryData)) {
                map.put(categoryData, new ArrayList<>());
            }
            map.get(categoryData).add(tagData);
        }

        return map;
    }
}

package project.reviewing.integration.tag.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.tag.command.domain.Category;
import project.reviewing.tag.command.domain.Tag;
import project.reviewing.tag.query.application.TagQueryService;
import project.reviewing.tag.query.application.response.CategoriesResponse;
import project.reviewing.tag.query.application.response.CategoryResponse;
import project.reviewing.tag.query.dao.data.CategoryData;
import project.reviewing.tag.query.dao.data.TagData;

@DisplayName("TagQueryService 는")
public class TagQueryServiceTest extends IntegrationTest {

    @DisplayName("정상적인 경우 태그 목록을 반환한다.")
    @Test
    void findTagWithCategory() {
        final TagQueryService sut = new TagQueryService(tagDao);
        final Category backend = createCategory(new Category("백엔드"));
        final Tag spring = createTag(new Tag("Spring", backend));
        final Tag java = createTag(new Tag("Java", backend));

        final CategoriesResponse actual = sut.findTags();

        assertThat(actual.getCategories()).hasSize(1)
                .usingRecursiveComparison()
                .isEqualTo(List.of(toCategoryResponse(backend, spring, java)));
    }

    private Category createCategory(final Category category) {
        final Category savedCategory = categoryRepository.save(category);
        entityManager.clear();
        return savedCategory;
    }

    private Tag createTag(final Tag tag) {
        final Tag savedTag = tagRepository.save(tag);
        entityManager.clear();
        return savedTag;
    }

    private CategoryResponse toCategoryResponse(final Category category, final Tag... tag) {
        final CategoryData categoryData = new CategoryData(category.getId(), category.getName());
        final List<TagData> tagData = Arrays.stream(tag)
                .map(t -> new TagData(t.getId(), t.getName()))
                .collect(Collectors.toList());

        return CategoryResponse.of(categoryData, tagData);
    }
}

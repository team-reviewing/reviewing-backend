package project.reviewing.integration.tag.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.tag.command.domain.Category;
import project.reviewing.tag.command.domain.Tag;
import project.reviewing.tag.query.dao.data.TagData;
import project.reviewing.tag.query.dao.data.TagWithCategoryData;

@DisplayName("TagDao 는")
public class TagDaoTest extends IntegrationTest {

    @DisplayName("해당 리뷰어의 기술 스택을 조회한다.")
    @Test
    void findTag() {
        final Category category = createCategory(new Category("백엔드"));
        createTag(new Tag("Spring", category));
        createTag(new Tag("Java", category));
        final Member member = new Member(1L, "username", "email@gmail.com", "image", "profile");
        final Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L, 2L), "안녕하세요");
        final Member savedMember = createMemberAndRegisterReviewer(member, reviewer);

        final List<TagData> actual = tagDao.findByReviewerId(savedMember.getReviewer().getId());

        assertThat(actual).hasSize(2)
                .extracting("id")
                .containsExactly(1L, 2L);
    }

    @DisplayName("카테고리와 태그가 존재하면 해당 값을 반환한다.")
    @Test
    void findCategoryAndTag() {
        final Category backend = createCategory(new Category("백엔드"));
        final Tag spring = createTag(new Tag("Spring", backend));
        final Tag java = createTag(new Tag("Java", backend));
        final Category frontend = createCategory(new Category("프론트엔드"));
        final Tag react = createTag(new Tag("React", frontend));
        final Tag javaScript = createTag(new Tag("JavaScript", frontend));
        final Tag typeScript = createTag(new Tag("TypeScript", frontend));

        final List<TagWithCategoryData> actual = tagDao.findAll();

        assertThat(actual).hasSize(5)
                .extracting("categoryId", "categoryName", "tagId", "tagName")
                .containsExactly(
                        tuple(backend.getId(), backend.getName(), spring.getId(), spring.getName()),
                        tuple(backend.getId(), backend.getName(), java.getId(), java.getName()),
                        tuple(frontend.getId(), frontend.getName(), react.getId(), react.getName()),
                        tuple(frontend.getId(), frontend.getName(), javaScript.getId(), javaScript.getName()),
                        tuple(frontend.getId(), frontend.getName(), typeScript.getId(), typeScript.getName())
                );
    }

    @DisplayName("카테고리와 태그가 존재하지 않으면 빈 값을 반환한다.")
    @Test
    void findNotExistCategoryAndTag() {
        final List<TagWithCategoryData> actual = tagDao.findAll();

        assertThat(actual).hasSize(0)
                .isEqualTo(List.of());
    }

    private Member createMemberAndRegisterReviewer(final Member member, final Reviewer reviewer) {
        final Member result = memberRepository.save(member);
        member.register(reviewer);
        entityManager.flush();
        entityManager.clear();
        return result;
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
}

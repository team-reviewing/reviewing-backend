package project.reviewing.integration.tag.dao;

import static org.assertj.core.api.Assertions.assertThat;

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
                .usingRecursiveFieldByFieldElementComparator()
                .extracting("id")
                .containsExactly(1L, 2L);
    }

    @DisplayName("카테고리와 태그가 존재하지 않으면 빈 값을 반환한다.")
    @Test
    void findNotExistCategoryAndTag() {
        final List<TagWithCategoryData> actual = tagDao.findAll();

        assertThat(actual).hasSize(0)
                .isEqualTo(List.of());
    }

    private Member createMemberAndRegisterReviewer(final Member member, final Reviewer reviewer) {
        member.register(reviewer);
        final Member result = memberRepository.save(member);
        entityManager.flush();
        return result;
    }

    private Category createCategory(final Category category) {
        final Category savedCategory = categoryRepository.save(category);
        entityManager.flush();
        return savedCategory;
    }

    private Tag createTag(final Tag tag) {
        final Tag savedTag = tagRepository.save(tag);
        entityManager.flush();
        return savedTag;
    }
}

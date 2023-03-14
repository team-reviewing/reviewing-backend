package project.reviewing.integration.member.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.tag.command.domain.Category;
import project.reviewing.tag.command.domain.CategoryRepository;
import project.reviewing.tag.command.domain.Tag;
import project.reviewing.tag.command.domain.TagRepository;
import project.reviewing.tag.query.dao.TagDao;
import project.reviewing.tag.query.dao.TagData;

@DisplayName("TagDao 는")
@DataJpaTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class TagDaoTest {

    @Autowired
    private TagDao tagDao;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private EntityManager entityManager;

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

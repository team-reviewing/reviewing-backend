package project.reviewing.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.member.query.dao.MyInformationDao;
import project.reviewing.member.query.dao.ReviewerDao;
import project.reviewing.review.command.domain.Review;
import project.reviewing.review.query.dao.ReviewsDAO;
import project.reviewing.tag.command.domain.Category;
import project.reviewing.tag.command.domain.CategoryRepository;
import project.reviewing.tag.command.domain.Tag;
import project.reviewing.tag.command.domain.TagRepository;
import project.reviewing.tag.query.dao.TagDao;
import project.reviewing.review.command.domain.ReviewRepository;

@DataJpaTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public abstract class IntegrationTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected TagRepository tagRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected ReviewRepository reviewRepository;

    @Autowired
    protected MyInformationDao myInformationDao;

    @Autowired
    protected ReviewerDao reviewerDao;

    @Autowired
    protected TagDao tagDao;

    @Autowired
    protected ReviewsDAO reviewsDAO;

    @Autowired
    protected TestEntityManager entityManager;

    protected Member createMember(final Member member) {
        final Member savedMember = memberRepository.save(member);
        entityManager.clear();
        return savedMember;
    }

    protected Member createMemberAndRegisterReviewer(final Member member, final Reviewer reviewer) {
        member.register(reviewer);
        final Member savedMember = memberRepository.save(member);
        entityManager.flush();
        entityManager.clear();
        return savedMember;
    }

    protected Category createCategory(final Category category) {
        final Category savedCategory = categoryRepository.save(category);
        entityManager.clear();
        return savedCategory;
    }

    protected Tag createTag(final Tag tag) {
        final Tag savedTag = tagRepository.save(tag);
        entityManager.clear();
        return savedTag;
    }

    protected Review createReview(final Review review) {
        final Review savedReview = reviewRepository.save(review);
        entityManager.clear();
        return savedReview;
    }
}

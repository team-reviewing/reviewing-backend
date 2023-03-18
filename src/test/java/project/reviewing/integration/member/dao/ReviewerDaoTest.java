package project.reviewing.integration.member.dao;

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
import project.reviewing.member.query.dao.data.MyReviewerInformationData;
import project.reviewing.tag.command.domain.Category;
import project.reviewing.tag.command.domain.Tag;
import project.reviewing.tag.query.dao.data.TagData;

@DisplayName("ReviewerDao 는")
public class ReviewerDaoTest extends IntegrationTest {

    @DisplayName("회원의 리뷰어 정보를 조회한다.")
    @Test
    void getReviewer() {
        final Member member = new Member(1L, "username", "email@gmail.com", "image", "profile");
        final Category category = createCategory(new Category("백엔드"));
        final Tag tag = createTag(new Tag("Java", category));
        final Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(tag.getId()), "안녕하세요");
        final Member savedMember = createMemberAndRegisterReviewer(member, reviewer);

        List<MyReviewerInformationData> actual = reviewerDao.findByMemberId(savedMember.getId());

        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .containsExactly(toReviewerInformationData(savedMember.getReviewer(), tag));
    }

    @DisplayName("회원이 존재하지 않으면 빈 값을 조회한다.")
    @Test
    void findNotExistMember() {
        final Long wrongMemberId = 1L;

        final List<MyReviewerInformationData> actual = reviewerDao.findByMemberId(wrongMemberId);

        assertThat(actual).isEmpty();
    }

    private MyReviewerInformationData toReviewerInformationData(final Reviewer reviewer, final Tag tag) {
        return new MyReviewerInformationData(
                reviewer.getJob().getValue(),
                reviewer.getCareer().getCareer(),
                reviewer.getIntroduction(),
                new TagData(tag.getId(), tag.getName())
        );
    }
}

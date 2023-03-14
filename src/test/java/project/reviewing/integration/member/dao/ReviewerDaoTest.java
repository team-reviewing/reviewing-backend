package project.reviewing.integration.member.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.member.query.dao.data.ReviewerData;

@DisplayName("ReviewerDao 는")
public class ReviewerDaoTest extends IntegrationTest {

    @DisplayName("회원의 리뷰어 정보를 조회한다.")
    @Test
    void getReviewer() {
        final Member member = new Member(1L, "username", "email@gmail.com", "image", "profile");
        final Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(1L, 2L), "안녕하세요");
        final Member savedMember = createMemberAndRegisterReviewer(member, reviewer);

        ReviewerData actual = reviewerDao.findByMemberId(savedMember.getId()).get();

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(toReviewerData(savedMember.getReviewer()));
    }

    @DisplayName("회원이 존재하지 않으면 빈 값을 조회한다.")
    @Test
    void findNotExistMember() {
        final Long wrongMemberId = 1L;

        final Optional<ReviewerData> actual = reviewerDao.findByMemberId(wrongMemberId);

        assertThat(actual).isEqualTo(Optional.empty());
    }

    private Member createMemberAndRegisterReviewer(final Member member, final Reviewer reviewer) {
        member.register(reviewer);
        return memberRepository.save(member);
    }

    private ReviewerData toReviewerData(final Reviewer reviewer) {
        return new ReviewerData(
                reviewer.getJob().getValue(),
                reviewer.getCareer().getCareer(),
                reviewer.getIntroduction()
        );
    }
}

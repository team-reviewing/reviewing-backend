package project.reviewing.integration.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.member.query.application.MemberQueryService;
import project.reviewing.member.query.application.response.MyInformationResponse;
import project.reviewing.member.query.application.response.ReviewerInformationResponse;
import project.reviewing.member.query.dao.data.MyInformation;
import project.reviewing.tag.command.domain.Category;
import project.reviewing.tag.command.domain.Tag;

@DisplayName("MemberQueryService 는")
public class MemberQueryServiceTest extends IntegrationTest {

    @DisplayName("내 정보 조회 시 ")
    @Nested
    class MemberFindTest {

        @DisplayName("정상적인 경우 회원을 조회한다.")
        @Test
        void findMember() {
            final MemberQueryService sut = new MemberQueryService(myInformationDao, reviewerDao, memberRepository, tagRepository);
            final Member member = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
            final Long memberId = createMember(member).getId();

            final MyInformationResponse actual = sut.findMember(memberId);

            assertThat(actual).usingRecursiveComparison().isEqualTo(toMyInformationResponse(member));
        }

        @DisplayName("회원이 존재하지 않는 경우 예외를 반환한다.")
        @Test
        void findNotExistMember() {
            final MemberQueryService sut = new MemberQueryService(myInformationDao, reviewerDao, memberRepository, tagRepository);
            final Long notExistMemberId = -1L;

            assertThatThrownBy(() -> sut.findMember(notExistMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
        }
    }

    @DisplayName("리뷰어 정보 및 선택 목록 조회 시")
    @Nested
    class ReviewerAndChoiceListFindTest {

        @DisplayName("정상적인 경우 회원의 리뷰어 정보와 선택 목록을 반환한다.")
        @Test
        void findReviewerAndChoiceList() {
            final MemberQueryService sut = new MemberQueryService(myInformationDao, reviewerDao, memberRepository, tagRepository);
            final Member member = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
            final Category category = createCategory(new Category("백엔드"));
            final Tag tag1 = createTag(new Tag("Java", category));
            final Tag tag2 = createTag(new Tag("Spring", category));
            final Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(tag1.getId(), tag2.getId()), "안녕하세요");
            final Member savedMember = createMemberAndRegisterReviewer(member, reviewer);

            final ReviewerInformationResponse actual = sut.findReviewerWithChoiceList(savedMember.getId());

            assertAll(
                    () -> assertThat(actual.getJob()).isEqualTo(Job.BACKEND.getValue()),
                    () -> assertThat(actual.getCareer()).isEqualTo(Career.JUNIOR.getCareer()),
                    () -> assertThat(actual.getIntroduction()).isEqualTo("안녕하세요"),
                    () -> assertThat(actual.getJobList())
                            .isEqualTo(Arrays.stream(Job.values()).map(Job::getValue).collect(Collectors.toList())),
                    () -> assertThat(actual.getCareerList())
                            .isEqualTo(Arrays.stream(Career.values()).map(Career::getCareer).collect(Collectors.toList()))
            );
        }

        @DisplayName("회원이 존재하지 않는 경우 예외를 반환한다.")
        @Test
        void findNotExistMember() {
            final MemberQueryService sut = new MemberQueryService(myInformationDao, reviewerDao, memberRepository, tagRepository);
            final Long notExistMemberId = -1L;

            assertThatThrownBy(() -> sut.findReviewerWithChoiceList(notExistMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
        }

        @DisplayName("회원이 리뷰어를 등록하지 않은 경우 빈 값을 반환한다.")
        @Test
        void findEmptyReviewer() {
            final MemberQueryService sut = new MemberQueryService(myInformationDao, reviewerDao, memberRepository, tagRepository);
            final Member member = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
            final Long memberId = createMember(member).getId();

            final ReviewerInformationResponse actual = sut.findReviewerWithChoiceList(memberId);

            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(ReviewerInformationResponse.empty());
        }
    }

    private MyInformationResponse toMyInformationResponse(final Member member) {
        return MyInformationResponse.of(toMyInformation(member));
    }

    private MyInformation toMyInformation(final Member member) {
        return new MyInformation(
                member.getUsername(), member.getEmail(),
                member.getImageUrl(), member.getProfileUrl(),
                member.isReviewer()
        );
    }
}

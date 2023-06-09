package project.reviewing.integration.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Career;
import project.reviewing.member.command.domain.Job;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.member.exception.MemberNotFoundException;
import project.reviewing.member.exception.ReviewerNotFoundException;
import project.reviewing.member.query.application.MemberQueryService;
import project.reviewing.member.query.application.response.*;
import project.reviewing.member.query.dao.data.MyInformationData;
import project.reviewing.member.query.dao.data.ReviewerData;
import project.reviewing.tag.command.domain.Category;
import project.reviewing.tag.command.domain.Tag;
import project.reviewing.tag.query.application.response.TagResponse;
import project.reviewing.tag.query.dao.data.TagData;

@DisplayName("MemberQueryService 는")
public class MemberQueryServiceTest extends IntegrationTest {

    private MemberQueryService memberQueryService;

    @BeforeEach
    void setUp() {
        memberQueryService = new MemberQueryService(myInformationDao, reviewerDao, memberRepository, tagRepository);
    }

    @DisplayName("내 정보 조회 시 ")
    @Nested
    class MemberFindTest {

        @DisplayName("정상적인 경우 회원을 조회한다.")
        @Test
        void findMember() {
            final Member member = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
            final Long memberId = createMember(member).getId();

            final MyInformationResponse actual = memberQueryService.findMember(memberId);

            assertThat(actual).usingRecursiveComparison().isEqualTo(toMyInformationResponse(member));
        }

        @DisplayName("회원이 존재하지 않는 경우 예외를 반환한다.")
        @Test
        void findNotExistMember() {
            final Long notExistMemberId = -1L;

            assertThatThrownBy(() -> memberQueryService.findMember(notExistMemberId))
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
            final Member member = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
            final Category category = createCategory(new Category("백엔드"));
            final Tag tag1 = createTag(new Tag("Java", category));
            final Tag tag2 = createTag(new Tag("Spring", category));
            final Reviewer reviewer = new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(tag1.getId(), tag2.getId()),
                    "안녕하세요");
            final Member savedMember = createMemberAndRegisterReviewer(member, reviewer);

            final MyReviewerInformationResponse actual = memberQueryService.findReviewerWithChoiceList(savedMember.getId());

            assertAll(
                    () -> assertThat(actual.getJob()).isEqualTo(Job.BACKEND.getValue()),
                    () -> assertThat(actual.getCareer()).isEqualTo(Career.JUNIOR.getCareer()),
                    () -> assertThat(actual.getIntroduction()).isEqualTo("안녕하세요"),
                    () -> assertThat(actual.getJobList())
                            .isEqualTo(Arrays.stream(Job.values()).map(Job::getValue).collect(Collectors.toList())),
                    () -> assertThat(actual.getCareerList())
                            .isEqualTo(
                                    Arrays.stream(Career.values()).map(Career::getCareer).collect(Collectors.toList()))
            );
        }

        @DisplayName("회원이 존재하지 않는 경우 예외를 반환한다.")
        @Test
        void findNotExistMember() {
            final Long notExistMemberId = -1L;

            assertThatThrownBy(() -> memberQueryService.findReviewerWithChoiceList(notExistMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
        }

        @DisplayName("회원이 리뷰어를 등록하지 않은 경우 빈 값을 반환한다.")
        @Test
        void findEmptyReviewer() {
            final Member member = new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile");
            final Long memberId = createMember(member).getId();

            final MyReviewerInformationResponse actual = memberQueryService.findReviewerWithChoiceList(memberId);

            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(MyReviewerInformationResponse.empty(
                            Arrays.stream(Job.values()).map(Job::getValue).collect(Collectors.toList()),
                            Arrays.stream(Career.values()).map(Career::getCareer).collect(Collectors.toList()),
                            tagRepository.findAll().stream().map(TagResponse::from).collect(Collectors.toList())
                    ));
        }
    }

    @DisplayName("단일 리뷰어 목록 조회 시 ")
    @Nested
    class SingleReviewerFindTest {

        @DisplayName("Reviewer Id로 조회한다.")
        @Test
        void findByReviewerId() {
            final Category category = createCategory(new Category("백엔드"));
            final Tag tag = createTag(new Tag("Java", category));
            final Member reviewerMember = createMemberAndRegisterReviewer(
                    new Member(2L, "bboor", "bboor@gmail.com", "imageUrl", "https://github.com/bboor"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(tag.getId()), "소개글")
            );

            reviewerMember.updateReviewerScore(3.5F);
            entityManager.merge(reviewerMember);
            entityManager.flush();
            entityManager.clear();

            final SingleReviewerResponse response = memberQueryService.findSingleReviewer(reviewerMember.getReviewer().getId());

            assertThat(response).usingRecursiveComparison()
                    .isEqualTo(makeSingleReviewerResponse(reviewerMember, tag));
        }

        @DisplayName("Reviewer Id로 조회했을 때 리뷰어 정보가 없으면 예외 반환한다.")
        @Test
        void findNotExistReviewerByReviewerId() {
            assertThatThrownBy(() -> memberQueryService.findSingleReviewer(1L))
                    .isInstanceOf(ReviewerNotFoundException.class);
        }
    }

    @DisplayName("리뷰어 목록 조회 시")
    @Nested
    class ReviewersFindTest {

        @DisplayName("카테고리와 태그를 입력하지 않은 경우 전체 리뷰어 목록을 반환한다.")
        @Test
        void findReviewers() {
            final Category backend = createCategory(new Category("백엔드"));
            final Tag java = createTag(new Tag("Java", backend));
            final Tag spring = createTag(new Tag("Spring", backend));
            final Category frontend = createCategory(new Category("프론트엔드"));
            final Tag react = createTag(new Tag("React", frontend));
            final Member savedMember1 = createMemberAndRegisterReviewer(
                    new Member(1L, "username1", "email@gmail.com", "image", "profile1"),
                    new Reviewer(Job.BACKEND, Career.SENIOR, Set.of(java.getId(), spring.getId()), "안녕하세요")
            );
            final Member savedMember2 = createMemberAndRegisterReviewer(
                    new Member(2L, "username2", "email@daum.com", "image", "profile2"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(spring.getId()), "안녕하세요")
            );
            final Member savedMember3 = createMemberAndRegisterReviewer(
                    new Member(3L, "username3", "email@naver.com", "image", "profile3"),
                    new Reviewer(Job.FRONTEND, Career.JUNIOR, Set.of(react.getId()), "안녕하세요")
            );

            final ReviewersResponse actual = memberQueryService.findReviewers(PageRequest.of(0, 3), null, null);

            assertAll(
                    () -> assertThat(actual.getReviewers()).hasSize(3)
                            .usingRecursiveComparison()
                            .isEqualTo(List.of(
                                    toReviewerResponse(savedMember1, java, spring),
                                    toReviewerResponse(savedMember2, spring),
                                    toReviewerResponse(savedMember3, react)
                            )),
                    () -> assertThat(actual.isHasNext()).isFalse()
            );
        }

        @DisplayName("카테고리를 입력하는 경우 해당 카테고리의 기술스택을 갖고 있는 리뷰어 목록을 반환한다.")
        @Test
        void findReviewersByCategory() {
            final Category backend = createCategory(new Category("백엔드"));
            final Tag java = createTag(new Tag("Java", backend));
            final Category frontend = createCategory(new Category("프론트엔드"));
            final Tag react = createTag(new Tag("React", frontend));
            final Member savedMember1 = createMemberAndRegisterReviewer(
                    new Member(1L, "username1", "email@gmail.com", "image", "profile1"),
                    new Reviewer(Job.BACKEND, Career.SENIOR, Set.of(java.getId()), "안녕하세요")
            );
            createMemberAndRegisterReviewer(
                    new Member(2L, "username2", "email@naver.com", "image", "profile2"),
                    new Reviewer(Job.FRONTEND, Career.JUNIOR, Set.of(react.getId()), "안녕하세요")
            );

            final ReviewersResponse actual = memberQueryService.findReviewers(PageRequest.of(0, 3), backend.getId(), null);

            assertAll(
                    () -> assertThat(actual.getReviewers()).hasSize(1)
                            .usingRecursiveComparison()
                            .isEqualTo(List.of(toReviewerResponse(savedMember1, java))),
                    () -> assertThat(actual.isHasNext()).isFalse()
            );
        }

        @DisplayName("태그를 입력하는 경우 해당 태그를 갖고 있는 리뷰어 목록을 반환한다.")
        @Test
        void findReviewersByTags() {
            final Category backend = createCategory(new Category("백엔드"));
            final Tag java = createTag(new Tag("Java", backend));
            final Tag spring = createTag(new Tag("Spring", backend));
            final Member savedMember1 = createMemberAndRegisterReviewer(
                    new Member(1L, "username1", "email@gmail.com", "image", "profile1"),
                    new Reviewer(Job.BACKEND, Career.SENIOR, Set.of(java.getId(), spring.getId()), "안녕하세요")
            );
            createMemberAndRegisterReviewer(
                    new Member(2L, "username2", "email@daum.com", "image", "profile2"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(spring.getId()), "안녕하세요")
            );

            final ReviewersResponse actual = memberQueryService.findReviewers(
                    PageRequest.of(0, 3), backend.getId(), List.of(java.getId())
            );

            assertAll(
                    () -> assertThat(actual.getReviewers()).hasSize(1)
                            .usingRecursiveComparison()
                            .isEqualTo(List.of(toReviewerResponse(savedMember1, java, spring))),
                    () -> assertThat(actual.isHasNext()).isFalse()
            );
        }

        @DisplayName("연속된 두 페이지를 조회할 때 중복되지 않은 리뷰어 목록을 반환한다.")
        @Test
        void findReviewersByTagsWhenContinuousPage() {
            final Category backend = createCategory(new Category("백엔드"));
            final Tag java = createTag(new Tag("Java", backend));
            final Tag spring = createTag(new Tag("Spring", backend));
            createMemberAndRegisterReviewer(
                    new Member(1L, "username1", "email@gmail.com", "image", "profile1"),
                    new Reviewer(Job.BACKEND, Career.SENIOR, Set.of(java.getId(), spring.getId()), "안녕하세요")
            );
            createMemberAndRegisterReviewer(
                    new Member(2L, "username2", "email@daum.com", "image", "profile2"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(java.getId()), "안녕하세요")
            );
            createMemberAndRegisterReviewer(
                    new Member(3L, "username3", "email@daum.com", "image", "profile3"),
                    new Reviewer(Job.BACKEND, Career.JUNIOR, Set.of(java.getId()), "안녕하세요")
            );

            final ReviewersResponse response1 = memberQueryService.findReviewers(PageRequest.of(0, 2), backend.getId(), null);
            final ReviewersResponse response2 = memberQueryService.findReviewers(
                    PageRequest.of(
                            response1.getReviewers().get(response1.getReviewers().size() - 1).getId().intValue(), 2
                    ),
                    backend.getId(), null
            );

            assertThat(response1.getReviewers()).hasSize(2)
                    .usingRecursiveFieldByFieldElementComparator()
                    .doesNotContainAnyElementsOf(response2.getReviewers());
        }
    }

    private MyInformationResponse toMyInformationResponse(final Member member) {
        return MyInformationResponse.of(toMyInformation(member));
    }

    private MyInformationData toMyInformation(final Member member) {
        return new MyInformationData(
                member.getUsername(), member.getEmail(),
                member.getImageUrl(), member.getProfileUrl(),
                member.isReviewer()
        );
    }

    private ReviewerResponse toReviewerResponse(final Member member, final Tag... tag) {
        final List<TagData> techStack = Arrays.stream(tag)
                .map(t -> new TagData(t.getId(), t.getName()))
                .collect(Collectors.toList());

        return ReviewerResponse.from(
                new ReviewerData(
                        member.getReviewer().getId(), member.getReviewer().getJob().getValue(),
                        member.getReviewer().getCareer().getCareer(), member.getReviewer().getIntroduction(),
                        member.getUsername(), member.getImageUrl(),
                        member.getProfileUrl(), techStack, member.getReviewer().getScore()
                )
        );
    }

    private SingleReviewerResponse makeSingleReviewerResponse(final Member reviewerMember, final Tag... tags) {
        return SingleReviewerResponse.from(
                new ReviewerData(
                        reviewerMember.getReviewer().getId(),
                        reviewerMember.getReviewer().getJob().getValue(),
                        reviewerMember.getReviewer().getCareer().getCareer(),
                        reviewerMember.getReviewer().getIntroduction(),
                        reviewerMember.getUsername(),
                        reviewerMember.getImageUrl(),
                        reviewerMember.getProfileUrl(),
                        Arrays.stream(tags)
                                .map(t -> new TagData(t.getId(), t.getName()))
                                .collect(Collectors.toList()),
                        reviewerMember.getReviewer().getScore()
                )
        );
    }
}

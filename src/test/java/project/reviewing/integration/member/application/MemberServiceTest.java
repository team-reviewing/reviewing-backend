package project.reviewing.integration.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.application.MemberService;
import project.reviewing.member.presentation.request.MyInformationUpdateRequest;
import project.reviewing.member.presentation.request.ReviewerRegistrationRequest;
import project.reviewing.member.presentation.request.ReviewerUpdateRequest;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.Reviewer;
import project.reviewing.member.exception.MemberNotFoundException;

@DisplayName("MemberService 는 ")
public class MemberServiceTest extends IntegrationTest {

    @DisplayName("내 정보 수정 시")
    @Nested
    class MemberUpdateTest {

        @DisplayName("정상적인 경우 내 정보를 수정한다.")
        @Test
        void updateMember() {
            final MemberService sut = new MemberService(memberRepository);
            final Member member = createMember(new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile"));
            final MyInformationUpdateRequest myInformationUpdateRequest = new MyInformationUpdateRequest("newUsername",
                    "newEmail@gmail.com");

            sut.update(member.getId(), myInformationUpdateRequest);
            entityManager.flush();
            entityManager.clear();

            final Member actual = getMember(member.getId());
            assertAll(
                    () -> assertThat(actual.getUsername()).isEqualTo("newUsername"),
                    () -> assertThat(actual.getEmail()).isEqualTo("newEmail@gmail.com")
            );
        }

        @DisplayName("회원이 존재하지 않는 경우 예외를 반환한다.")
        @Test
        void updateNotExistMember() {
            final MemberService sut = new MemberService(memberRepository);
            final Long notExistMemberId = -1L;
            final MyInformationUpdateRequest myInformationUpdateRequest = new MyInformationUpdateRequest("newUsername",
                    "newEmail@gmail.com");

            assertThatThrownBy(() -> sut.update(notExistMemberId, myInformationUpdateRequest))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
        }
    }

    @DisplayName("리뷰어 등록 시 ")
    @Nested
    class ReviewerRegistrationTest {

        @DisplayName("정상적인 경우 리뷰어를 등록한다.")
        @Test
        void registerReviewer() {
            final MemberService sut = new MemberService(memberRepository);
            final Member member = createMember(new Member(1L, "username12", "email@gmail.com", "image.png", "github.com/profile"));
            final ReviewerRegistrationRequest reviewerRegistrationRequest = new ReviewerRegistrationRequest(
                    "백엔드", "신입(1년 이하)", List.of(1L, 2L), "자기 소개입니다."
            );

            sut.registerReviewer(member.getId(), reviewerRegistrationRequest);
            entityManager.flush();
            entityManager.clear();

            final Member actual = getMember(member.getId());
            assertAll(
                    () -> assertThat(actual.isReviewer()).isTrue(),
                    () -> assertThat(actual.getReviewer().getId()).isNotNull(),
                    () -> assertThat(actual.getReviewer()).usingRecursiveComparison()
                            .ignoringFields("id", "member")
                            .isEqualTo(reviewerRegistrationRequest.toEntity()),
                    () -> assertThat(actual.getReviewer().getMember().getId()).isEqualTo(member.getId())
            );
        }

        @DisplayName("회원이 존재하지 않는 경우 예외를 반환한다.")
        @Test
        void registerReviewerByNotExistMember() {
            final MemberService sut = new MemberService(memberRepository);
            final Long wrongMemberId = -1L;
            final ReviewerRegistrationRequest reviewerRegistrationRequest = new ReviewerRegistrationRequest(
                    "백엔드", "신입(1년 이하)", List.of(1L, 2L), "자기 소개입니다."
            );

            assertThatThrownBy(() -> sut.registerReviewer(wrongMemberId, reviewerRegistrationRequest))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
        }
    }

    @DisplayName("리뷰어 수정 시 ")
    @Nested
    class ReviewerUpdateTest {

        @DisplayName("정상적인 경우 리뷰어를 수정한다.")
        @Test
        void updateReviewer() {
            final MemberService sut = new MemberService(memberRepository);
            final Member member = createMember(new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile"));
            final ReviewerRegistrationRequest reviewerRegistrationRequest = new ReviewerRegistrationRequest(
                    "백엔드", "신입(1년 이하)", List.of(1L, 2L), "자기 소개입니다."
            );
            registerReviewer(sut, member.getId(), reviewerRegistrationRequest);
            final ReviewerUpdateRequest reviewerUpdateRequest = new ReviewerUpdateRequest(
                    "프론트엔드", "주니어(1 ~ 3년)", List.of(1L, 2L, 3L), "자기 소개입니다."
            );

            sut.updateReviewer(member.getId(), reviewerUpdateRequest);
            entityManager.flush();
            entityManager.clear();

            final Reviewer actual = getMember(member.getId()).getReviewer();
            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("id", "member")
                    .isEqualTo(reviewerUpdateRequest.toEntity());
        }

        @DisplayName("회원이 존재하지 않는 경우 예외를 반환한다.")
        @Test
        void updateReviewerByNotExistMember() {
            final MemberService sut = new MemberService(memberRepository);
            final Member member = createMember(new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile"));
            final ReviewerRegistrationRequest reviewerRegistrationRequest = new ReviewerRegistrationRequest(
                    "백엔드", "신입(1년 이하)", List.of(1L, 2L), "자기 소개입니다."
            );
            registerReviewer(sut, member.getId(), reviewerRegistrationRequest);
            final Long wrongMemberId = -1L;
            final ReviewerUpdateRequest reviewerUpdateRequest = new ReviewerUpdateRequest(
                    "프론트엔드", "주니어(1 ~ 3년)", List.of(1L, 2L, 3L), "자기 소개입니다."
            );

            assertThatThrownBy(() -> sut.updateReviewer(wrongMemberId, reviewerUpdateRequest))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
        }
    }

    @DisplayName("리뷰어 활성화/비활성화 시")
    @Nested
    class ReviewerStatusChangeTest {

        @DisplayName("정상적인 경우 리뷰어 상태를 변경할 수 있다.")
        @Test
        void changeStatus() {
            final MemberService sut = new MemberService(memberRepository);
            final Member member = createMember(new Member(1L, "username", "email@gmail.com", "image.png", "github.com/profile"));
            final ReviewerRegistrationRequest reviewerRegistrationRequest = new ReviewerRegistrationRequest(
                    "백엔드", "신입(1년 이하)", List.of(1L, 2L), "자기 소개입니다."
            );
            registerReviewer(sut, member.getId(), reviewerRegistrationRequest);

            sut.changeReviewerStatus(member.getId());
            entityManager.flush();
            entityManager.clear();

            final Member actual = getMember(member.getId());
            assertThat(actual.isReviewer()).isFalse();
        }
    }

    private void registerReviewer(final MemberService sut, final Long id, final ReviewerRegistrationRequest request) {
        sut.registerReviewer(id, request);
        entityManager.flush();
        entityManager.clear();
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }
}

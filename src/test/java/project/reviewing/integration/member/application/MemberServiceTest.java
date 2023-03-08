package project.reviewing.integration.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.reviewing.member.query.response.MyInformationResponse;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.command.application.MemberService;
import project.reviewing.member.command.application.request.UpdatingMemberRequest;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;

@DisplayName("MemberService 는 ")
@DataJpaTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("내 정보 수정 시")
    @Nested
    class MemberUpdateTest {

        @DisplayName("정상적인 경우 내 정보를 수정한다.")
        @Test
        void updateMember() {
            final MemberService sut = new MemberService(memberRepository);
            final Member member = createMember(new Member(1L, "username", "email@gmail.com", "image.png"));
            final UpdatingMemberRequest updatingMemberRequest = new UpdatingMemberRequest("newUsername",
                    "newEmail@gmail.com");

            sut.update(member.getId(), updatingMemberRequest);

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
            final Long notExistMemberId = 1L;
            final UpdatingMemberRequest updatingMemberRequest = new UpdatingMemberRequest("newUsername",
                    "newEmail@gmail.com");

            assertThatThrownBy(() -> sut.update(notExistMemberId, updatingMemberRequest))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
        }
    }

    @DisplayName("내 정보 조회 시 ")
    @Nested
    class MemberFindTest {

        @DisplayName("정상적인 경우 회원을 조회한다.")
        @Test
        void findMember() {
            final MemberService sut = new MemberService(memberRepository);
            final Member member = new Member(1L, "username", "email@gmail.com", "image.png");
            final Long memberId = createMember(member).getId();

            final MyInformationResponse actual = sut.findMember(memberId);

            assertThat(actual).usingRecursiveComparison().isEqualTo(toResponse(member));
        }

        @DisplayName("회원이 존재하지 않는 경우 예외를 반환한다.")
        @Test
        void findNotExistMember() {
            final MemberService sut = new MemberService(memberRepository);
            final Long notExistMemberId = 1L;

            assertThatThrownBy(() -> sut.findMember(notExistMemberId))
                    .isInstanceOf(MemberNotFoundException.class)
                    .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
        }
    }

    private Member createMember(final Member member) {
        return memberRepository.save(member);
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
    }

    private MyInformationResponse toResponse(final Member member) {
        return MyInformationResponse.of(member);
    }
}

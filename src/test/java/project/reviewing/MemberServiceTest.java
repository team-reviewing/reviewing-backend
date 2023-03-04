package project.reviewing;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.application.MemberService;
import project.reviewing.member.application.request.UpdatingMemberRequest;
import project.reviewing.member.domain.MemberRepository;
import project.reviewing.member.exception.MemberNotFoundException;

@DisplayName("MemberService 는 ")
@DataJpaTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("회원이 존재하지 않는 경우 예외를 반환한다.")
    @Test
    void updateNotExistMember() {
        final MemberService sut = new MemberService(memberRepository);
        final Long notExistMemberId = 1L;
        final UpdatingMemberRequest updatingMemberRequest = new UpdatingMemberRequest("newUsername", "newEmail@gmail.com");

        assertThatThrownBy(() -> sut.update(notExistMemberId, updatingMemberRequest))
                .isInstanceOf(MemberNotFoundException.class)
                .hasMessage(ErrorType.MEMBER_NOT_FOUND.getMessage());
    }
}

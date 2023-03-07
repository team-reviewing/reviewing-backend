package project.reviewing.member.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import project.reviewing.common.annotation.ApplicationTest;
import project.reviewing.member.application.response.MemberResponse;
import project.reviewing.member.domain.Member;
import project.reviewing.member.domain.MemberRepository;
import project.reviewing.member.domain.Role;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ApplicationTest
public class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
    }

    @DisplayName("유저의 기본 정보를 가져온다.")
    @Test
    void findMemberProfileTest() {
        // given
        final Long memberId = 1L;
        final String imageURL = "image_url";

        createMember(memberId, 123L, "username", "email", imageURL, "githubURL", Role.ROLE_USER);

        // when
        final MemberResponse memberResponse = memberService.findMemberProfile(memberId);

        // then
        assertAll(
                () -> assertThat(memberResponse).isNotNull(),
                () -> assertThat(memberResponse.getImageURL()).isEqualTo(imageURL)
        );
    }

    private Member createMember(
            final Long memberId, final Long githubId, final String username, final String email,
            final String imageURL, final String githubURL, final Role role
    ) {
        return memberRepository.save(Member.builder()
                .id(memberId)
                .githubId(githubId)
                .username(username)
                .email(email)
                .imageURL(imageURL)
                .githubURL(githubURL)
                .role(role)
                .build());
    }
}

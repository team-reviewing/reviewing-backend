package project.reviewing.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.reviewing.auth.application.response.LoginResponse;
import project.reviewing.auth.domain.Profile;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.common.annotation.ApplicationTest;
import project.reviewing.member.domain.Member;
import project.reviewing.member.domain.MemberRepository;
import project.reviewing.member.domain.Role;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ApplicationTest
public class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private OauthClient oauthClient;

    @Mock
    private TokenProvider tokenProvider;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(memberRepository, refreshTokenRepository, oauthClient, tokenProvider);
    }

    @DisplayName("Github Authorization Code를 받아 회원 가입한다.")
    @Test
    void githubJoinMemberTest() {
        // given
        final String authorizationCode = "code";
        final Long memberId = 1L;
        final Profile profile = new Profile(1L, "Tom", "Tom@gmail.com", "imageURL", "https://github.com/Tom");

        final String accessToken = "accessToken";
        final RefreshToken refreshToken = new RefreshToken(memberId, "refreshToken", 600L);

        final LoginResponse expectedResponse = new LoginResponse(memberId, accessToken, refreshToken.getTokenString(), true);

        given(oauthClient.getProfileByAuthorizationCode(authorizationCode))
                .willReturn(profile);
        given(tokenProvider.createAccessToken(1L, Role.ROLE_USER))
                .willReturn(accessToken);
        given(tokenProvider.createRefreshToken(1L, Role.ROLE_USER))
                .willReturn(refreshToken);

        // when
        final LoginResponse loginResponse = authService.githubLogin(authorizationCode);

        // then
        assertThat(loginResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @DisplayName("Github Authorization Code를 받아 로그인 한다.")
    @Test
    void githubLoginTest() {
        // given
        final String authorizationCode = "code";
        final Member member = createMember(1L, 1L, "Tom", "Tom@gmail.com", Role.ROLE_USER);
        final Profile profile = new Profile(member.getId(), member.getUsername(), member.getEmail(), "imageURL", member.getGithubURL());

        final String accessToken = "accessToken";
        final RefreshToken refreshToken = new RefreshToken(member.getId(), "refreshToken", 600L);

        final LoginResponse expectedResponse = new LoginResponse(member.getId(), accessToken, refreshToken.getTokenString(), false);

        given(oauthClient.getProfileByAuthorizationCode(authorizationCode))
                .willReturn(profile);
        given(tokenProvider.createAccessToken(member.getId(), Role.ROLE_USER))
                .willReturn(accessToken);
        given(tokenProvider.createRefreshToken(member.getId(), Role.ROLE_USER))
                .willReturn(refreshToken);

        // when
        final LoginResponse loginResponse = authService.githubLogin(authorizationCode);

        // then
        assertThat(loginResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @DisplayName("Github의 계정 이름을 바꾼 회원의 로그인 과정에서 Github URL을 갱신한다.")
    @Test
    void githubProfileUpdateTest() {
        // given
        final String authorizationCode = "code";
        final Member member = createMember(1L, 1L, "Tom", "Tom@gmail.com", Role.ROLE_USER);

        final Member expectedMember = Member.builder()
                .id(member.getId())
                .githubId(member.getGithubId())
                .username("Bob")
                .githubURL("Bob@gmail.com")
                .role(Role.ROLE_USER)
                .build();
        final Profile profile = new Profile(
                expectedMember.getId(), expectedMember.getUsername(),
                expectedMember.getEmail(), "imageURL", expectedMember.getGithubURL()
        );

        final String accessToken = "accessToken";
        final RefreshToken refreshToken = new RefreshToken(member.getId(), "refreshToken", 600L);

        given(oauthClient.getProfileByAuthorizationCode(authorizationCode))
                .willReturn(profile);
        given(tokenProvider.createAccessToken(member.getId(), Role.ROLE_USER))
                .willReturn(accessToken);
        given(tokenProvider.createRefreshToken(member.getId(), Role.ROLE_USER))
                .willReturn(refreshToken);

        // when
        authService.githubLogin(authorizationCode);
        final Optional<Member> updatedMember = memberRepository.findById(member.getId());

        // then
        assertAll(
                () -> assertThat(updatedMember.isPresent()).isEqualTo(true),
                () -> assertThat(updatedMember.get().getGithubURL()).isEqualTo(expectedMember.getGithubURL())
        );
    }

    private Member createMember(
            final Long memberId, final Long githubId, final String username, final String githubURL, final Role role
    ) {
        return memberRepository.save(Member.builder()
                .id(memberId)
                .githubId(githubId)
                .username(username)
                .githubURL(githubURL)
                .role(role)
                .build());
    }
}

package project.reviewing.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import project.reviewing.auth.application.response.GithubLoginResponse;
import project.reviewing.auth.infrastructure.response.Profile;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.common.annotation.ApplicationTest;
import project.reviewing.member.domain.Member;
import project.reviewing.member.domain.MemberRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        final Profile profile = new Profile(memberId, "Tom", "Tom@gmail.com", "imageURL", "https://github.com/Tom");

        final String accessToken = "accessToken";
        final RefreshToken refreshToken = new RefreshToken(memberId, "refreshToken", 600L);

        final GithubLoginResponse expectedResponse = new GithubLoginResponse(memberId, accessToken, refreshToken.getTokenString(), true);

        given(oauthClient.getProfileByAuthorizationCode(authorizationCode)).willReturn(profile);
        given(tokenProvider.createAccessToken(memberId)).willReturn(accessToken);
        given(tokenProvider.createRefreshToken(memberId)).willReturn(refreshToken);

        // when
        final GithubLoginResponse githubLoginResponse = authService.githubLogin(authorizationCode);

        // then
        assertThat(githubLoginResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @DisplayName("Github Authorization Code를 받아 로그인 한다.")
    @Test
    void githubLoginTest() {
        // given
        final String authorizationCode = "code";
        final Member member = createMember(1L, 1L, "Tom", "Tom@gmail.com", "https://github.com/image");
        final Profile profile = new Profile(member.getId(), member.getUsername(), member.getEmail(), "imageURL", member.getGithubURL());

        final String accessToken = "accessToken";
        final RefreshToken refreshToken = new RefreshToken(member.getId(), "refreshToken", 600L);

        final GithubLoginResponse expectedResponse = new GithubLoginResponse(member.getId(), accessToken, refreshToken.getTokenString(), false);

        given(oauthClient.getProfileByAuthorizationCode(authorizationCode)).willReturn(profile);
        given(tokenProvider.createAccessToken(member.getId())).willReturn(accessToken);
        given(tokenProvider.createRefreshToken(member.getId())).willReturn(refreshToken);

        // when
        final GithubLoginResponse githubLoginResponse = authService.githubLogin(authorizationCode);

        // then
        assertThat(githubLoginResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @DisplayName("Github의 계정 이름을 바꾼 회원의 로그인 과정에서 Github URL을 갱신한다.")
    @Test
    void githubProfileUpdateTest() {
        // given
        final String authorizationCode = "code";
        final Member member = createMember(1L, 1L, "Tom", "Tom@gmail.com", "https://github.com/Tom/image");

        final Member expectedMember = Member.builder()
                .id(member.getId())
                .githubId(member.getGithubId())
                .username("Bob")
                .email("Bob@gmail.com")
                .githubURL("https://github.com/Bob/image")
                .build();
        final Profile profile = new Profile(
                expectedMember.getId(), expectedMember.getUsername(),
                expectedMember.getEmail(), "imageURL", expectedMember.getGithubURL()
        );

        final String accessToken = "accessToken";
        final RefreshToken refreshToken = new RefreshToken(member.getId(), "refreshToken", 600L);

        given(oauthClient.getProfileByAuthorizationCode(authorizationCode)).willReturn(profile);
        given(tokenProvider.createAccessToken(member.getId())).willReturn(accessToken);
        given(tokenProvider.createRefreshToken(member.getId())).willReturn(refreshToken);

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
            final Long memberId, final Long githubId, final String username, final String email, final String githubURL
    ) {
        return memberRepository.save(Member.builder()
                .id(memberId)
                .githubId(githubId)
                .username(username)
                .email(email)
                .githubURL(githubURL)
                .build());
    }
}

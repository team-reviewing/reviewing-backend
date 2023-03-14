package project.reviewing.integration.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import project.reviewing.auth.application.AuthService;
import project.reviewing.auth.application.OauthClient;
import project.reviewing.auth.application.response.GithubLoginResponse;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.auth.infrastructure.response.Profile;
import project.reviewing.integration.IntegrationTest;
import project.reviewing.member.command.domain.Member;

public class AuthServiceTest extends IntegrationTest {

    @Mock
    private OauthClient oauthClient;

    @Mock
    private TokenProvider tokenProvider;

    @DisplayName("Github Authorization Code를 받아 로그인 한다.")
    @Test
    void githubLoginTest() {
        // given
        final AuthService authService = new AuthService(memberRepository, refreshTokenRepository, oauthClient, tokenProvider);
        final String authorizationCode = "code";
        final Member member = createMember(
                new Member(1L, "Tom", "Tom@gmail.com", "https://github.com/image", "https://github.com/Tom")
        );
        final Profile profile = new Profile(
                member.getGithubId(), member.getUsername(), member.getEmail(), "imageUrl", member.getProfileUrl()
        );
        final String accessToken = "accessToken";
        final RefreshToken refreshToken = new RefreshToken(member.getId(), "refreshToken", 600L);

        final GithubLoginResponse expectedResponse = new GithubLoginResponse(
                member.getId(), accessToken, refreshToken.getToken()
        );

        given(oauthClient.getProfileByAuthorizationCode(authorizationCode)).willReturn(profile);
        given(tokenProvider.createAccessToken(member.getId())).willReturn(accessToken);
        given(tokenProvider.createRefreshToken(member.getId())).willReturn(refreshToken);

        // when
        final GithubLoginResponse githubLoginResponse = authService.loginGithub(authorizationCode);

        // then
        assertThat(githubLoginResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @DisplayName("Github의 계정 이름을 바꾼 회원의 로그인 과정에서 Github URL을 갱신한다.")
    @Test
    void githubProfileUpdateTest() {
        // given
        final AuthService authService = new AuthService(memberRepository, refreshTokenRepository, oauthClient, tokenProvider);
        final String authorizationCode = "code";
        final Member member = createMember(
                new Member(1L, "Tom", "Tom@gmail.com", "https://github.com/Tom/image", "https://github.com/Tom")
        );
        final Member expectedMember = new Member(
                member.getGithubId(), "Bob", "Bob@gmail.com",
                "https://github.com/Bob/image", "https://github.com/Bob"
        );
        final Profile profile = new Profile(
                expectedMember.getGithubId(), expectedMember.getUsername(),
                expectedMember.getEmail(), "imageUrl", expectedMember.getProfileUrl()
        );
        final String accessToken = "accessToken";
        final RefreshToken refreshToken = new RefreshToken(member.getId(), "refreshToken", 600L);

        given(oauthClient.getProfileByAuthorizationCode(authorizationCode)).willReturn(profile);
        given(tokenProvider.createAccessToken(member.getId())).willReturn(accessToken);
        given(tokenProvider.createRefreshToken(member.getId())).willReturn(refreshToken);

        // when
        authService.loginGithub(authorizationCode);
        final Member updatedMember = memberRepository.findById(member.getId()).get();

        // then
        assertAll(
                () -> assertThat(updatedMember.getProfileUrl()).isEqualTo(expectedMember.getProfileUrl())
        );
    }

    private Member createMember(final Member member) {
        final Member savedMember = memberRepository.save(member);
        entityManager.clear();
        return savedMember;
    }
}

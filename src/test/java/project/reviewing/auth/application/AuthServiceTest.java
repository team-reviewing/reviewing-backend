package project.reviewing.auth.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import project.reviewing.auth.application.response.LoginGithubResponse;
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

    @DisplayName("Github Authorization Code를 받아 로그인 한다.")
    @Test
    void githubLoginTest() {
        // given
        authService = new AuthService(memberRepository, refreshTokenRepository, oauthClient, tokenProvider);
        final String authorizationCode = "code";
        final Member member = createMember(
                1L, "Tom", "Tom@gmail.com", "https://github.com/image", "https://github.com/Tom", "소개글"
        );
        final Profile profile = new Profile(
                member.getGithubId(), member.getUsername(), member.getEmail(), "imageUrl", member.getGithubUrl()
        );
        final String accessToken = "accessToken";
        final RefreshToken refreshToken = new RefreshToken(member.getId(), "refreshToken", 600L);

        final LoginGithubResponse expectedResponse = new LoginGithubResponse(
                member.getId(), accessToken, refreshToken.getToken()
        );

        given(oauthClient.getProfileByAuthorizationCode(authorizationCode)).willReturn(profile);
        given(tokenProvider.createAccessToken(member.getId())).willReturn(accessToken);
        given(tokenProvider.createRefreshToken(member.getId())).willReturn(refreshToken);

        // when
        final LoginGithubResponse loginGithubResponse = authService.loginGithub(authorizationCode);

        // then
        assertThat(loginGithubResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }

    @DisplayName("Github의 계정 이름을 바꾼 회원의 로그인 과정에서 Github URL을 갱신한다.")
    @Test
    void githubProfileUpdateTest() {
        // given
        authService = new AuthService(memberRepository, refreshTokenRepository, oauthClient, tokenProvider);
        final String authorizationCode = "code";
        final Member member = createMember(
                1L, "Tom", "Tom@gmail.com", "https://github.com/Tom/image", "https://github.com/Tom", "소개글"
        );
        final Member expectedMember = new Member(
                member.getGithubId(), "Bob", "Bob@gmail.com",
                "https://github.com/Bob/image", "https://github.com/Bob", "소개글"
        );
        final Profile profile = new Profile(
                expectedMember.getGithubId(), expectedMember.getUsername(),
                expectedMember.getEmail(), "imageUrl", expectedMember.getGithubUrl()
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
                () -> assertThat(updatedMember.getGithubUrl()).isEqualTo(expectedMember.getGithubUrl())
        );
    }

    private Member createMember(
            final Long githubId, final String username, final String email,
            final String imageUrl, final String githubUrl, final String introduction
    ) {
        return memberRepository.save(new Member(githubId, username, email, imageUrl, githubUrl, introduction));
    }
}

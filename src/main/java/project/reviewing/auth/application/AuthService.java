package project.reviewing.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.auth.application.response.GithubLoginResponse;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.auth.infrastructure.response.Profile;
import project.reviewing.member.command.domain.Member;
import project.reviewing.member.command.domain.MemberRepository;


@Transactional
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OauthClient oauthClient;
    private final TokenProvider tokenProvider;

    public GithubLoginResponse loginGithub(final String authorizationCode) {
        final Profile profile = oauthClient.getProfileByAuthorizationCode(authorizationCode);
        final Member loginMember = convertProfileToMember(profile);
        final Member member = memberRepository.findByGithubId(profile.getId())
                .orElseGet(() -> memberRepository.save(loginMember));

        member.updateLoginInformation(loginMember);

        final String accessToken = tokenProvider.createAccessToken(member.getId());
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(member.getId());

        refreshTokenRepository.save(refreshToken);
        return new GithubLoginResponse(member.getId(), accessToken, refreshToken.getToken());
    }

    public RefreshResponse refreshTokens(final Long memberId) {
        final String accessToken = tokenProvider.createAccessToken(memberId);
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(memberId);

        refreshTokenRepository.save(refreshToken);
        return new RefreshResponse(accessToken, refreshToken.getToken());
    }

    public void removeRefreshToken(final Long memberId) {
        refreshTokenRepository.deleteById(memberId);
    }

    private Member convertProfileToMember(final Profile profile) {
        return new Member(
                profile.getId(), profile.getUsername(), profile.getEmail(),
                profile.getImageUrl(), profile.getGithubUrl()
        );
    }
}

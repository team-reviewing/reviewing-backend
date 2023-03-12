package project.reviewing.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.auth.application.response.GithubLoginResponse;
import project.reviewing.auth.application.response.RefreshResponse;
import project.reviewing.auth.infrastructure.response.Profile;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.domain.RefreshTokenRepository;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.domain.Member;
import project.reviewing.member.domain.MemberRepository;
import project.reviewing.member.exception.MemberException;


@Transactional
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OauthClient oauthClient;
    private final TokenProvider tokenProvider;

    public GithubLoginResponse githubLogin(final String authorizationCode) {
        boolean isCreated = false;

        final Profile profile = oauthClient.getProfileByAuthorizationCode(authorizationCode);
        Member member = memberRepository.findByGithubId(profile.getId());

        if (member == null) {
            member = memberRepository.save(Member.builder()
                    .githubId(profile.getId())
                    .username(profile.getUsername())
                    .email(profile.getEmail())
                    .imageURL(profile.getImageURL())
                    .githubURL(profile.getGithubURL())
                    .introduction("")
                    .build());
            isCreated = true;
        } else {
            member.updateGithubURL(profile.getGithubURL());
        }

        if (member == null) {
            throw new MemberException(ErrorType.NOT_FOUND_MEMBER);
        }

        final String accessToken = tokenProvider.createAccessToken(member.getId());
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(member.getId());

        refreshTokenRepository.save(refreshToken);
        return new GithubLoginResponse(member.getId(), accessToken, refreshToken.getTokenString(), isCreated);
    }

    public RefreshResponse refreshTokens(final Long memberId) {
        final String accessToken = tokenProvider.createAccessToken(memberId);
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(memberId);

        refreshTokenRepository.save(refreshToken);
        return new RefreshResponse(accessToken, refreshToken.getTokenString());
    }

    public void removeRefreshToken(final Long memberId) {
        refreshTokenRepository.deleteById(memberId);
    }
}

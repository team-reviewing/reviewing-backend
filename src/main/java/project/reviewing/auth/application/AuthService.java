package project.reviewing.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.reviewing.auth.application.response.LoginResponse;
import project.reviewing.auth.domain.Profile;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.infrastructure.TokenProvider;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.domain.Member;
import project.reviewing.member.domain.MemberRepository;
import project.reviewing.member.domain.Role;
import project.reviewing.member.exception.MemberException;


@Transactional
@RequiredArgsConstructor
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OauthClient oauthClient;
    private final TokenProvider tokenProvider;

    public LoginResponse githubLogin(final String authorizationCode) {
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
                    .role(Role.ROLE_USER)
                    .build());
            isCreated = true;
        } else {
            member.updateGithubURL(profile.getGithubURL());
        }

        if (member == null) {
            throw new MemberException(ErrorType.NOT_FOUND_MEMBER);
        }

        final String accessToken = tokenProvider.createAccessToken(member.getId(), member.getRole());
        final RefreshToken refreshToken = tokenProvider.createRefreshToken(member.getId(), member.getRole());

        refreshTokenRepository.save(refreshToken);
        return new LoginResponse(member.getId(), accessToken, refreshToken.getTokenString(), isCreated);
    }

    public void refresh() {
        /*
            cookie에 token이 담겨있지 않다면 문제 발생.
            refresh token db에서 조회.
            데이터가 없다면 문제 발생.
            데이터가 있다면 검증.
            검증 과정은 그냥 토큰이 같은지만 검사하면 된다.
            만료 여부는 어차피 cookie로 줄 때, 만료 시간을 refresh token의 만료 시간으로 줄거기 때문에
            만료가 되었다면 cookie에 담기지 않아 이전에 문제 발생.
            검증이 끝난다면 access token과 refresh token을 재발급하고 refresh token을 db에 저장하고
            두 token을 client로 전달.
         */
    }
}

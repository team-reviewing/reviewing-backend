package project.reviewing.auth.infrastructure;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.exception.InvalidTokenException;
import project.reviewing.auth.exception.RefreshTokenException;
import project.reviewing.common.exception.ErrorType;
import project.reviewing.member.domain.Role;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenProvider {

    private final SecretKey accessTokenSecretKey;
    private final SecretKey refreshTokenSecretKey;
    private final long accessTokenValidTime;
    private final long refreshTokenValidTime;

    public TokenProvider(
            @Value(value = "${jwt.access-token.secret-key}") final String accessTokenSecretKey,
            @Value(value = "${jwt.refresh-token.secret-key}") final String refreshTokenSecretKey,
            @Value(value = "${jwt.access-token.valid-time}") final long accessTokenValidTime,
            @Value(value = "${jwt.refresh-token.valid-time}") final long refreshTokenValidTime
    ) {
        this.accessTokenSecretKey = Keys.hmacShaKeyFor(accessTokenSecretKey.getBytes(StandardCharsets.UTF_8));
        this.refreshTokenSecretKey = Keys.hmacShaKeyFor(refreshTokenSecretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidTime = accessTokenValidTime;
        this. refreshTokenValidTime = refreshTokenValidTime;
    }

    public String createAccessToken(final Long memberId, final Role role) {
        return createJwt(memberId, role, accessTokenValidTime, accessTokenSecretKey);
    }
    public RefreshToken createRefreshToken(final Long memberId, final Role role) {
        return new RefreshToken(
                memberId, createJwt(memberId, role, refreshTokenValidTime, refreshTokenSecretKey), new Date().getTime()
        );
    }

    public Claims parseAccessToken(String accessToken) {
        try {
            return parseJwt(accessToken, accessTokenSecretKey);
        } catch (JwtException e) {
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN);
        }
    }
    public Claims parseRefreshToken(String refreshToken) {
        try {
            return parseJwt(refreshToken, refreshTokenSecretKey);
        } catch (JwtException e) {
            throw new RefreshTokenException(ErrorType.INVALID_TOKEN);
        }
    }

    public String createAccessTokenUsingTime(final Long memberId, final Role role, final long validTime) {
        return createJwt(memberId, role, validTime, accessTokenSecretKey);
    }
    public String createRefreshTokenUsingTime(final Long memberId, final Role role, final long validTime) {
        return createJwt(memberId, role, validTime, refreshTokenSecretKey);
    }

    private String createJwt(final Long memberId, final Role role, final long validTime, final SecretKey secretKey) {
        final Claims claims = Jwts.claims();
        claims.put("id", memberId);
        claims.put("role", role);

        final Date now = new Date();

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims parseJwt(String jwt, SecretKey secretKey) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    public long getAccessTokenValidTime() {
        return accessTokenValidTime;
    }
    public long getRefreshTokenValidTime() {
        return refreshTokenValidTime;
    }
}

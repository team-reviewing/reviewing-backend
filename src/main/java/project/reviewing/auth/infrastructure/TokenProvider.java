package project.reviewing.auth.infrastructure;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.auth.exception.InvalidTokenException;
import project.reviewing.common.exception.ErrorType;

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
        this.refreshTokenValidTime = refreshTokenValidTime;
    }

    public String createAccessToken(final Long memberId) {
        return createJwt(memberId, accessTokenValidTime, accessTokenSecretKey);
    }

    public RefreshToken createRefreshToken(final Long memberId) {
        return new RefreshToken(
                memberId, createJwt(memberId, refreshTokenValidTime, refreshTokenSecretKey), refreshTokenValidTime
        );
    }

    public Long parseAccessToken(final String accessToken) {
        try {
            return parseJwt(accessToken, accessTokenSecretKey);
        } catch (JwtException e) {
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN);
        }
    }

    public Long parseRefreshToken(final String refreshToken) {
        try {
            return parseJwt(refreshToken, refreshTokenSecretKey);
        } catch (JwtException e) {
            throw new InvalidTokenException(ErrorType.INVALID_TOKEN);
        }
    }

    public String createAccessTokenUsingTime(final long memberId, final long validTime) {
        return createJwt(memberId, validTime, accessTokenSecretKey);
    }

    public String createRefreshTokenStringUsingTime(final long memberId, final long validTime) {
        return createJwt(memberId, validTime, refreshTokenSecretKey);
    }

    private String createJwt(final long memberId, final long validTime, final SecretKey secretKey) {
        final Claims claims = Jwts.claims();
        claims.put("id", memberId);

        final Date now = new Date();

        return Jwts.builder()
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    private Long parseJwt(final String jwt, final SecretKey secretKey) throws JwtException {
        return Long.valueOf(
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody()
                        .get("id")
                        .toString()
        );
    }

    public long getRefreshTokenValidTime() {
        return refreshTokenValidTime;
    }
}

package project.reviewing.auth.infrastructure;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.reviewing.auth.domain.RefreshToken;
import project.reviewing.member.domain.Role;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenProvider {

    private SecretKey secretKey;
    private final long accessTokenValidTime;
    private final long refreshTokenValidTime;

    public TokenProvider(
            @Value(value = "${jwt.secret-key}") final String secretKey,
            @Value(value = "${jwt.access-token.valid-time}") final long accessTokenValidTime,
            @Value(value = "${jwt.refresh-token.valid-time}") final long refreshTokenValidTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidTime = accessTokenValidTime;
        this. refreshTokenValidTime = refreshTokenValidTime;
    }

    public String createAccessToken(final Long memberId, final Role role) {
        return createJwt(memberId, role, accessTokenValidTime);
    }

    public RefreshToken createRefreshToken(final Long memberId, final Role role) {
        return new RefreshToken(memberId, createJwt(memberId, role, refreshTokenValidTime), new Date().getTime());
    }

    public String createJwt(final Long memberId, final Role role, final long validTime) {
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

    boolean validateJwt(String jwt) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt);
        } catch (JwtException e) {
            throw new JwtException("토큰 검증 실패");
        }
        return false;
    }

    public long getAccessTokenValidTime() {
        return accessTokenValidTime;
    }
    public long getRefreshTokenValidTime() {
        return refreshTokenValidTime;
    }
}

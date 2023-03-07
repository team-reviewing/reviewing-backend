package project.reviewing.auth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "token_string", nullable = false, unique = true)
    private String tokenString;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    public RefreshToken(final Long memberId, final String tokenString, final Long issuedAt) {
        this.memberId = memberId;
        this.tokenString = tokenString;
        this.issuedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(issuedAt), TimeZone.getDefault().toZoneId());
    }
}

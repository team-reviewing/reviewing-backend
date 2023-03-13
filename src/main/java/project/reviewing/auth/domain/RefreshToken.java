package project.reviewing.auth.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RefreshToken {

    @Id
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private LocalDateTime issuedAt;

    public RefreshToken(final Long memberId, final String token, final Long issuedAt) {
        this.id = memberId;
        this.token = token;
        this.issuedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(issuedAt), TimeZone.getDefault().toZoneId());
    }
}

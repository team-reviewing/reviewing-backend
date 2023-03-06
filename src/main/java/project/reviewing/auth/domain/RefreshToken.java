package project.reviewing.auth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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
    private Long issuedAt;

    public RefreshToken(final Long memberId, final String tokenString, final Long issuedAt) {
        this.memberId = memberId;
        this.tokenString = tokenString;
        this.issuedAt = issuedAt;
    }
}

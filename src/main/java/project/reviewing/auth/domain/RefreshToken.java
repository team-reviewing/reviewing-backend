package project.reviewing.auth.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private final Long memberId;

    private final String token;

    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    private final long validTime;

    public RefreshToken(final Long memberId, final String token, final long validTime) {
        this.memberId = memberId;
        this.token = token;
        this.validTime = validTime;
    }
}

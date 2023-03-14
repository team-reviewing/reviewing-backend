package project.reviewing.auth.presentation;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import project.reviewing.auth.exception.InvalidAuthenticationException;
import project.reviewing.common.exception.ErrorType;

@RequestScope
@Component
public class AuthContext {

    private Long id;

    public Long getId() {
        if (id == null) {
            throw new InvalidAuthenticationException(ErrorType.NOT_AUTHENTICATED);
        }
        return id;
    }

    public void setId(final Long id) {
        if (id == null) {
            throw new InvalidAuthenticationException(ErrorType.ALREADY_AUTHENTICATED);
        }
        this.id = id;
    }
}

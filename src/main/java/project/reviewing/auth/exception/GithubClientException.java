package project.reviewing.auth.exception;

import project.reviewing.common.exception.ErrorType;

public class GithubClientException extends RuntimeException {

    public GithubClientException(final ErrorType errorType) {
        super(errorType.getMessage());
    }
}

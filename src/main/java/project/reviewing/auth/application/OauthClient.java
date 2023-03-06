package project.reviewing.auth.application;

import project.reviewing.auth.domain.Profile;

public interface OauthClient {
    Profile getProfileByAuthorizationCode(final String authorizationCode);
}

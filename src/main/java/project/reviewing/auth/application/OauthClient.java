package project.reviewing.auth.application;

import project.reviewing.auth.infrastructure.response.Profile;

public interface OauthClient {
    Profile getProfileByAuthorizationCode(String authorizationCode);
}

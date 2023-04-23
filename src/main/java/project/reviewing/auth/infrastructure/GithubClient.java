package project.reviewing.auth.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import project.reviewing.auth.application.OauthClient;
import project.reviewing.auth.infrastructure.response.Profile;
import project.reviewing.auth.exception.GithubClientException;
import project.reviewing.auth.infrastructure.request.GithubAuthRequest;
import project.reviewing.auth.infrastructure.response.GithubAuthResponse;
import project.reviewing.auth.infrastructure.response.GithubProfileResponse;
import project.reviewing.common.exception.ErrorType;

@Component
public class GithubClient implements OauthClient {

    private static final String GITHUB_BASE_URI = "https://github.com";
    private static final String API_GITHUB_BASE_URI = "https://api.github.com";
    private static final String AUTH_URI = "/login/oauth/access_token";
    private static final String USER_PROFILE_URI = "/user";

    private final String CLIENT_ID;
    private final String CLLIENT_SECRETS;
    private final WebClient authWebClient;
    private final WebClient profileWebClient;

    private final ObjectMapper objectMapper;

    public GithubClient(
            @Value("${github.api.client-id}") final String clientId,
            @Value("${github.api.client-secrets}") final String clientSecrets,
            final ObjectMapper objectMapper,
            final WebClient.Builder builder
    ) {
        this.CLIENT_ID = clientId;
        this.CLLIENT_SECRETS = clientSecrets;
        this.objectMapper = objectMapper;
        authWebClient = builder.baseUrl(GITHUB_BASE_URI).build();
        profileWebClient = builder.baseUrl(API_GITHUB_BASE_URI).build();
    }

    @Override
    public Profile getProfileByAuthorizationCode(String authorizationCode) {
        final GithubProfileResponse githubProfileResponse = findGithubUserProfile(
                findGithubAccessToken(authorizationCode).getAccessToken()
        );

        return new Profile(
                githubProfileResponse.getId(), githubProfileResponse.getLogin(), githubProfileResponse.getEmail(),
                githubProfileResponse.getAvatarUrl(), githubProfileResponse.getHtmlUrl()
        );
    }

    private GithubAuthResponse findGithubAccessToken(final String authorizationCode) {
        try {
            return authWebClient.post()
                    .uri(AUTH_URI)
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(
                            objectMapper.writeValueAsString(
                                    new GithubAuthRequest(CLIENT_ID, CLLIENT_SECRETS, authorizationCode)
                            )
                    )
                    .header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON))
                    .retrieve()
                    .bodyToMono(GithubAuthResponse.class)
                    .block();
        } catch (RuntimeException | JsonProcessingException e) {
            throw new GithubClientException(ErrorType.API_FAILED);
        }
    }

    private GithubProfileResponse findGithubUserProfile(final String accessToken) {
        try {
            return profileWebClient.get()
                    .uri(USER_PROFILE_URI)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("X-GitHub-Api-Version", "2022-11-28")
                    .retrieve()
                    .bodyToMono(GithubProfileResponse.class)
                    .block();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            throw new GithubClientException(ErrorType.API_FAILED);
        }
    }
}

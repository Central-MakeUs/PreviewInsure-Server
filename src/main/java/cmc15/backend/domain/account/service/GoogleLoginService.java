package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Platform;
import cmc15.backend.domain.account.response.GoogleTokenResponse;
import cmc15.backend.domain.account.response.GoogleUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static cmc15.backend.domain.account.entity.Platform.GOOGLE;

@Service
@RequiredArgsConstructor
public class GoogleLoginService implements OAuth2Service {


    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Value("${google.client-id}")
    private static final String GOOGLE_CLIENT_ID = "844048132781-dontbc8q2ornskbo6q71p2p8qu21d51p.apps.googleusercontent.com";

    @Value("${google.client-secret}")
    private static final String GOOGLE_SECRET = "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";

    @Value("${google.redirect-url}")
    private String REDIRECT_URI;

    @Override
    public Platform suppots() {
        return GOOGLE;
    }

    @Override
    public String toOAuthEntityResponse(final Platform platform, final String authorizationCode, final String appleToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_SECRET);
        params.add("code", authorizationCode);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token", request, GoogleTokenResponse.class);
        System.out.println(response.getBody().getAccessToken());

        //

        headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody().getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<GoogleUserResponse> userInfoResponse = restTemplate.exchange("https://www.googleapis.com/oauth2/v2/userinfo", HttpMethod.GET, entity, GoogleUserResponse.class);
        GoogleUserResponse googleUser = userInfoResponse.getBody();

        //

        return googleUser.getEmail();
    }

    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}

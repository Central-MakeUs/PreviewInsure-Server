package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.config.GoogleSettings;
import cmc15.backend.domain.account.config.OAuthSettings;
import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.Platform;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.response.GoogleTokenResponse;
import cmc15.backend.domain.account.response.GoogleUserResponse;
import cmc15.backend.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static cmc15.backend.domain.account.entity.Authority.ROLE_USER;
import static cmc15.backend.domain.account.entity.Platform.GOOGLE;

@Service
@RequiredArgsConstructor
public class GoogleLoginService implements OAuth2Service {


    public static final String GOOGLE_AUTH_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    public static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final GoogleSettings googleSettings;
    private final OAuthSettings oAuthSettings;

    @Override
    public Platform suppots() {
        return GOOGLE;
    }

    @Override
    public AccountResponse.OAuthConnection toSocialLoginAccount(final Platform platform, final String authorizationCode) {
        GoogleUserResponse googleUser = exchangeGoogleUser(authorizationCode);
        Optional<Account> optionalAccount = accountRepository.findByEmail(googleUser.getEmail());

        System.out.println("logger : " + oAuthSettings.getPassword());
        System.out.println("logger : " + oAuthSettings.getNonEncryptionPassword());

        Account account = optionalAccount.orElseGet(() ->
                accountRepository.save(Account.builder()
                        .email(googleUser.getEmail())
                        .password(passwordEncoder.encode(oAuthSettings.getNonEncryptionPassword()))
                        .authority(ROLE_USER)
                        .build()));

        String atk = tokenProvider.createAccessToken(account.getAccountId(), getAuthentication(account.getEmail(), oAuthSettings.getNonEncryptionPassword()));
        String rtk = tokenProvider.createRefreshToken(account.getEmail());

        return AccountResponse.OAuthConnection.to(account, atk, rtk);
    }

    // 구글 토큰으로 유저 정보 가져오기
    private GoogleUserResponse exchangeGoogleUser(String authorizationCode) {
        HttpEntity<String> entity = new HttpEntity<>(postForGoogleToken(authorizationCode));
        ResponseEntity<GoogleUserResponse> userInfoResponse = restTemplate.exchange(GOOGLE_AUTH_URL, HttpMethod.GET, entity, GoogleUserResponse.class);
        GoogleUserResponse googleUser = userInfoResponse.getBody();
        return googleUser;
    }

    // 인가코드로 구글 토큰 발급
    private HttpHeaders postForGoogleToken(String authorizationCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", googleSettings.getClientId());
        params.add("client_secret", googleSettings.getClientSecret());
        params.add("code", authorizationCode);
        params.add("redirect_uri", googleSettings.getRedirectUrl());
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<GoogleTokenResponse> response = restTemplate.postForEntity(GOOGLE_TOKEN_URL, request, GoogleTokenResponse.class);

        headers = new HttpHeaders();
        headers.setBearerAuth(response.getBody().getAccessToken());
        return headers;
    }

    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}

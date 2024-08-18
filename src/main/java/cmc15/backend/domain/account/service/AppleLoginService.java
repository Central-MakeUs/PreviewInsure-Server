package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.config.AppleSettings;
import cmc15.backend.domain.account.config.OAuthSettings;
import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.Platform;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.response.AppleLoginResponse;
import cmc15.backend.domain.account.response.AppleSocialTokenInfoResponse;
import cmc15.backend.global.config.jwt.TokenDecoder;
import cmc15.backend.global.config.jwt.TokenProvider;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import static cmc15.backend.domain.account.entity.Authority.ROLE_USER;
import static cmc15.backend.domain.account.entity.Platform.APPLE;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class AppleLoginService implements OAuth2Service {

    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String APPLE_AUTH_URL = "https://appleid.apple.com/auth/token";
    private final TokenProvider tokenProvider;
    private final RestTemplate restTemplate;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AppleSettings appleSettings;
    private final OAuthSettings oAuthSettings;
    private final AccountRepository accountRepository;

    @Override
    public Platform suppots() {
        return APPLE;
    }

    /**
     * @return AccountResponse.OAuthConnection
     * @apiNote 애플 로그인 API
     */
    @Override
    @Transactional
    public AccountResponse.OAuthConnection toSocialLoginAccount(Platform platform, String code) {
        AppleSocialTokenInfoResponse response = exchangeAppleSocialToken(appleSettings.getClientId(), generateClientSecret(), AUTHORIZATION_CODE, code);
        String idToken = response.getIdToken();
        AppleLoginResponse appleLoginResponse = TokenDecoder.decodePayload(idToken, AppleLoginResponse.class);

        Optional<Account> optionalAccount = accountRepository.findByAppleAccount(appleLoginResponse.getSub());
        Account account = optionalAccount.orElseGet(() ->
                accountRepository.save(Account.builder()
                        .email(appleLoginResponse.getEmail())
                        .password(oAuthSettings.getPassword())
                        .appleAccount(appleLoginResponse.getSub())
                        .authority(ROLE_USER)
                        .build())
        );

        String atk = tokenProvider.createAccessToken(account.getAccountId(), getAuthentication(account.getEmail(), oAuthSettings.getNonEncryptionPassword()));
        String rtk = tokenProvider.createRefreshToken(account.getEmail());
        String nickname = account.getNickName() == null ? "none" : encode(account.getNickName(), UTF_8);
        String successUrl = appleSettings.getRedirectUrl() + atk + "&nickname=" + encode(nickname, UTF_8);

        return AccountResponse.OAuthConnection.toRedirect(account, atk, rtk, successUrl);
    }

    private AppleSocialTokenInfoResponse exchangeAppleSocialToken(String clientId, String clientSecret, String grantType, String code) {
        // 요청 파라미터 설정
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(APPLE_AUTH_URL)
                .queryParam("client_id", clientId)
                .queryParam("client_secret", clientSecret)
                .queryParam("grant_type", grantType)
                .queryParam("code", code);

        // HTTP 헤더 설정 (필요시 추가)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        // 요청 엔터티 생성
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // POST 요청 수행
        ResponseEntity<AppleSocialTokenInfoResponse> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.POST,
                entity,
                AppleSocialTokenInfoResponse.class);

        // 응답 반환
        return response.getBody();
    }

    private String generateClientSecret() {

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, appleSettings.getKeyId())
                .setIssuer(appleSettings.getTeamId())
                .setAudience(appleSettings.getAuthUrl())
                .setSubject(appleSettings.getClientId())
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey getPrivateKey() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(appleSettings.getPrivateKey());
            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }
    }

    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}

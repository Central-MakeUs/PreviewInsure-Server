package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.config.NicknameSettings;
import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.Platform;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.global.config.jwt.TokenDecoder;
import cmc15.backend.global.config.jwt.TokenProvider;
import cmc15.backend.global.exception.CustomException;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.Security;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

import static cmc15.backend.domain.account.entity.Authority.ROLE_USER;
import static cmc15.backend.global.Result.NOT_FOUND_USER;
import static cmc15.backend.global.Result.NOT_MATCHED_PLATFORM;
import static java.net.URLEncoder.encode;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final RestTemplate restTemplate;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final NicknameSettings nicknameSettings;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<OAuth2Service> oAuth2Services;

    @Value("${apple.auth-url}")
    private String appleAuthUrl;

    @Value("${apple.client-id}")
    private String clientId;

    @Value("${apple.key-id}")
    private String keyId;

    @Value("${apple.team-id}")
    private String teamId;

    @Value("${apple.private-key}")
    private String privateKey;

    @Value("${apple.redirect-success}")
    private String redirectUrl;

    @Value("${oauth.password}")
    private String defaultPassword;

    @Value("${oauth.non-encryption-password}")
    private String defaultNonPassword;

    public static final int FIX_AGE_DAY = 1;

    /**
     * @param platform
     * @param code
     * @apiNote 소셜 로그인 API
     */
    @Transactional
    public AccountResponse.OAuthConnection socialLogin(final Platform platform, final String code) {
        for (OAuth2Service oAuth2Service : oAuth2Services) {
            if (oAuth2Service.suppots().equals(platform)) {
                String oAuthEmail = oAuth2Service.toOAuthEntityResponse(platform, code);

                Optional<Account> optionalAccount = accountRepository.findByEmail(oAuthEmail);
                Account account = optionalAccount.orElseGet(() ->
                        accountRepository.save(Account.builder()
                                .email(oAuthEmail)
                                .password(defaultPassword)
                                .authority(ROLE_USER)
                                .build()));

                String atk = tokenProvider.createAccessToken(account.getAccountId(), getAuthentication(account.getEmail(), defaultNonPassword));
                String rtk = tokenProvider.createRefreshToken(account.getEmail());

                return AccountResponse.OAuthConnection.to(account, optionalAccount.isPresent(), atk, rtk);
            }
        }

        throw new CustomException(NOT_MATCHED_PLATFORM);
    }

    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /**
     * @return AccountResponse.Connection
     * @apiNote 회원가입 API / 현재 사용되지 않음
     */
    @Transactional
    public AccountResponse.Connection accountRegister(final AccountRequest.Register request) {
        // 계정 등록
        Account account = saveAccount(request);
        // 토큰 발급
        String atk = tokenProvider.createAccessToken(account.getAccountId(), getAuthentication(account.getEmail(), request.getPassword()));
        String rtk = tokenProvider.createRefreshToken(account.getEmail());

        return AccountResponse.Connection.to(account, atk, rtk);
    }

    private Account saveAccount(AccountRequest.Register request) {
        return accountRepository.save(Account.builder()
                .nickName(request.getNickName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .authority(ROLE_USER)
                .build());
    }

    /**
     * @return String
     * @apiNote 랜덤 닉네임 생성 API
     */
    public AccountResponse.NickName createNickName() {
        Random random = new Random();
        String[] lastNames = nicknameSettings.getLastName();
        String[] firstNames = nicknameSettings.getFirstName();

        int part1Index = random.nextInt(lastNames.length);
        int part2Index = random.nextInt(firstNames.length);

        String nickName = lastNames[part1Index] + " " + firstNames[part2Index];

        return AccountResponse.NickName.to(nickName);
    }

    /**
     * @return void
     * @apiNote 나이 입력 API
     */
    @Transactional
    public Void updateAge(final Long accountId, final AccountRequest.Age request) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        return account.updateAge(calculateAge(request));
    }

    // 나이 계산
    private static int calculateAge(AccountRequest.Age request) {
        LocalDate birthDate = LocalDate.of(request.getYear(), request.getMonth(), FIX_AGE_DAY);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    /**
     * @return AccountResponse.OAuthConnection
     * @apiNote 애플 로그인 API
     */
    @Transactional
    public String appleLogin(String code) throws UnsupportedEncodingException {
        AppleSocialTokenInfoResponse response = getIdToken(clientId, generateClientSecret(), "authorization_code", code);
        String idToken = response.getIdToken();
        AppleIdTokenPayload appleIdTokenPayload = TokenDecoder.decodePayload(idToken, AppleIdTokenPayload.class);

        Optional<Account> optionalAccount = accountRepository.findByAppleAccount(appleIdTokenPayload.getSub());
        Account account = optionalAccount.orElseGet(() ->
                accountRepository.save(Account.builder()
                        .email(appleIdTokenPayload.getEmail())
                        .password("$2a$10$7NPHBBkAuyWG/lJz6Yv8/.n099SecuAwWkQq4DMkxeVKWl/R7o5.2")
                        .appleAccount(appleIdTokenPayload.getSub())
                        .authority(ROLE_USER)
                        .build())
        );

        String atk = tokenProvider.createAccessToken(account.getAccountId(), getAuthentication(account.getEmail(), "abc123"));
        String rtk = tokenProvider.createRefreshToken(account.getEmail());

        if (optionalAccount.isPresent()) {
            // 회원가입은 했는데 닉네임이 null 인 경우
            if (account.getNickName() == null) {
                return redirectUrl + atk + "&nickname=none";
            }
            return redirectUrl + atk + "&nickname=" + encode(account.getNickName(), UTF_8);
        } else {
            return redirectUrl + atk + "&nickname=null";
        }

    }

    public AppleSocialTokenInfoResponse getIdToken(String clientId, String clientSecret, String grantType, String code) {
        // URL 생성
        String url = "https://appleid.apple.com/auth/token";

        // 요청 파라미터 설정
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
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
                AppleSocialTokenInfoResponse.class
        );

        // 응답 반환
        return response.getBody();
    }

    private String generateClientSecret() {

        LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);

        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, keyId)
                .setIssuer(teamId)
                .setAudience(appleAuthUrl)
                .setSubject(clientId)
                .setExpiration(Date.from(expiration.atZone(ZoneId.systemDefault()).toInstant()))
                .setIssuedAt(new Date())
                .signWith(getPrivateKey(), SignatureAlgorithm.ES256)
                .compact();
    }

    private PrivateKey getPrivateKey() {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");

        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);

            PrivateKeyInfo privateKeyInfo = PrivateKeyInfo.getInstance(privateKeyBytes);
            return converter.getPrivateKey(privateKeyInfo);
        } catch (Exception e) {
            throw new RuntimeException("Error converting private key from String", e);
        }

    }

    /**
     * @param accountId
     * @param request
     * @return Void
     * @apiNote 닉네임 업데이트 API
     */
    @Transactional
    public Void updateNickname(Long accountId, final AccountRequest.Nickname request) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        account.updateNickname(request.getNickname());
        return null;
    }

    /**
     * @param accountId
     * @return
     * @apiNote 회원탈퇴 API
     */
    @Transactional
    public Void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
        return null;
    }

    public AccountResponse.Detail readAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        return AccountResponse.Detail.to(account);
    }
}

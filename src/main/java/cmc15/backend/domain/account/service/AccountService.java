package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.AccountInsurance;
import cmc15.backend.domain.account.entity.Platform;
import cmc15.backend.domain.account.repository.AccountInsuranceRepository;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.validator.AccountServiceValidator;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final RestTemplate restTemplate;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountServiceValidator accountServiceValidator;
    private final AccountRepository accountRepository;
    private final AccountInsuranceRepository accountInsuranceRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<OAuth2Service> oAuth2Services;

    @Value("${nickname.word1}")
    private String namesPart1;

    @Value("${nickname.word2}")
    private String namesPart2;

    @Value("${apple.auth-url}")
    private String appleAuthUrl;

    @Value("${apple.client-id}")
    private String clientId;

    @Value("${apple.redirect-url}")
    private String redirectUri;

    @Value("${apple.key-id}")
    private String keyId;

    @Value("${apple.key-path}")
    private String keyPath;

    @Value("${apple.team-id}")
    private String teamId;

    @Value("${apple.private-key}")
    private String privateKey;

    public static final int FIX_AGE_DAY = 1;

    /**
     * @return AccountResponse.Connection
     * @apiNote 회원가입 API
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

    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /**
     * @return String
     * @apiNote 랜덤 닉네임 생성 API
     */
    public AccountResponse.NickName createNickName() {
        Random random = new Random();
        String[] split1 = namesPart1.split(",");
        String[] split2 = namesPart2.split(",");

        int part1Index = random.nextInt(split1.length);
        int part2Index = random.nextInt(split2.length);

        String nickName = split1[part1Index] + " " + split2[part2Index];

        return AccountResponse.NickName.to(nickName);
    }

    /**
     * @return void
     * @apiNote 나이 입력 API
     */
    @Transactional
    public Void updateAge(Long accountId, final AccountRequest.Age request) {
        int age = calculateAge(request);

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        account.updateAge(age);
        return null;
    }

    // 나이 계산
    private static int calculateAge(AccountRequest.Age request) {
        LocalDate birthDate = LocalDate.of(request.getYear(), request.getMonth(), FIX_AGE_DAY);
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate, currentDate).getYears();
    }

    /**
     * @return void
     * @apiNote 인슈보딩 입력 API
     */
    @Transactional
    public Void updateInsureBoarding(final Long accountId, final AccountRequest.InsureBoarding request) {
        accountServiceValidator.validateEmptyGender(request);
        Account account = updateGender(accountId, request);
        saveInsureBoard(request, account);
        return null;
    }

    private void saveInsureBoard(AccountRequest.InsureBoarding request, Account account) {
        List<AccountRequest.InsureBoarding.InsureBoard> insureBoards = request.getInsureBoards();
        if (insureBoards.size() == 0) return;

        for (AccountRequest.InsureBoarding.InsureBoard insureBoard : insureBoards) {
            boolean isInsuranceAlreadyExists = accountInsuranceRepository.existsByAccountAndInsuranceTypeAndInsuranceCompany(account, insureBoard.getInsuranceType(), insureBoard.getInsuranceCompany());
            accountServiceValidator.validateAlreadyAddInsurance(isInsuranceAlreadyExists);

            accountInsuranceRepository.save(AccountInsurance.builder()
                    .account(account)
                    .insuranceType(insureBoard.getInsuranceType())
                    .insuranceCompany(insureBoard.getInsuranceCompany())
                    .build());
        }
    }

    private Account updateGender(Long accountId, AccountRequest.InsureBoarding request) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        account.updateGender(request.getGender());
        return account;
    }

    /**
     * @param platform
     * @param code
     * @param appleToken
     * @return
     */
    @Transactional
    public AccountResponse.OAuthConnection socialLogin(final Platform platform, final String code, final String appleToken) {
        String oAuthEmail = "";
        for (OAuth2Service oAuth2Service : oAuth2Services) {
            if (oAuth2Service.suppots().equals(platform)) {
                oAuthEmail = oAuth2Service.toOAuthEntityResponse(platform, code, appleToken);
            }
        }

        if (oAuthEmail.isBlank()) throw new CustomException(NOT_MATCHED_PLATFORM);

        Optional<Account> optionalAccount = accountRepository.findByEmail(oAuthEmail);
        String email = oAuthEmail;
        Account account = optionalAccount.orElseGet(() ->
                accountRepository.save(Account.builder()
                        .email(email)
                        .password("$2a$10$7NPHBBkAuyWG/lJz6Yv8/.n099SecuAwWkQq4DMkxeVKWl/R7o5.2")
                        .authority(ROLE_USER)
                        .build())
        );

        String atk = tokenProvider.createAccessToken(account.getAccountId(), getAuthentication(account.getEmail(), "abc123"));
        String rtk = tokenProvider.createRefreshToken(account.getEmail());

        return AccountResponse.OAuthConnection.to(account, optionalAccount.isPresent(), atk, rtk);

    }

    /**
     * @return List<AccountResponse.Insurances>
     * @apiNote 내가 가입한 보험 조회 API
     */
    public List<AccountResponse.Insurances> readAccountInsurances(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        List<AccountInsurance> accountInsurances = accountInsuranceRepository.findByAccount(account);

        return accountInsurances.stream().map(AccountResponse.Insurances::to).toList();
    }

    /**
     * @param request
     * @return AccountResponse.OAuthConnection
     * @apiNote 애플 로그인 API
     */
    @Transactional
    public String appleLogin(String code) {
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

        String url;
        if (optionalAccount.isPresent())
            url = "https://preview-insure-web-git-dev-sehuns-projects.vercel.app/callback/apple?token=" + atk;
        else
            url = "https://preview-insure-web-git-dev-sehuns-projects.vercel.app/callback/apple?token=" + atk + "&nickname=" + account.getNickName();

        log.info(url);
        return url;
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
}

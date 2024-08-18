package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.config.NicknameSettings;
import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.Platform;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.global.config.jwt.TokenProvider;
import cmc15.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Random;

import static cmc15.backend.global.Result.NOT_FOUND_USER;
import static cmc15.backend.global.Result.NOT_MATCHED_PLATFORM;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final NicknameSettings nicknameSettings;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final List<OAuth2Service> oAuth2Services;

    public static final int FIX_AGE_DAY = 1;

    /**
     * @param platform
     * @param code
     * @apiNote 소셜 로그인 API
     */
    @Transactional
    public AccountResponse.OAuthConnection socialLogin(final Platform platform, final String code) {
        return oAuth2Services.stream().filter(oAuth2Service -> oAuth2Service.suppots().equals(platform))
                .findFirst()
                .map(oAuth2Service -> oAuth2Service.toSocialLoginAccount(platform, code))
                .orElseThrow(() -> new CustomException(NOT_MATCHED_PLATFORM));
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
     * @param accountId
     * @param request
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
     * @apiNote 회원탈퇴 API
     */
    @Transactional
    public Void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
        return null;
    }

    /**
     * @param accountId
     * @apiNote 내 정보 조회 API
     */
    public AccountResponse.Detail readAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        return AccountResponse.Detail.to(account);
    }
}

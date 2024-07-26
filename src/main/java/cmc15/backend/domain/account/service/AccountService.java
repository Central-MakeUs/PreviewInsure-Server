package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.AccountInsurance;
import cmc15.backend.domain.account.repository.AccountInsuranceRepository;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.validator.AccountServiceValidator;
import cmc15.backend.global.config.jwt.TokenProvider;
import cmc15.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Random;

import static cmc15.backend.domain.account.entity.Authority.ROLE_USER;
import static cmc15.backend.global.Result.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountServiceValidator accountServiceValidator;
    private final AccountRepository accountRepository;
    private final AccountInsuranceRepository accountInsuranceRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${nickname.word1}")
    private String namesPart1;

    @Value("${nickname.word2}")
    private String namesPart2;

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
}

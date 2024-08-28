package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.AccountInsurance;
import cmc15.backend.domain.account.repository.AccountInsuranceRepository;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.request.AccountRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static cmc15.backend.domain.account.entity.Authority.ROLE_USER;
import static cmc15.backend.domain.account.entity.InsuranceType.LF;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AccountInsuranceServiceTest {

    @Autowired
    private AccountInsuranceService accountInsuranceService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountInsuranceRepository accountInsuranceRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("내 보험 취소 API_성공")
    @Test
    void 내_보험_취소_API_성공() {
        // given
        Account account = accountRepository.save(getAccountBuild("test3@test.com", "테스트3"));
        AccountInsurance accountInsurance = accountInsuranceRepository.save(AccountInsurance.builder()
                .account(account)
                .insuranceType(LF)
                .insuranceCompany("하나손해보험")
                .build());

        AccountRequest.DeleteInsurance request = new AccountRequest.DeleteInsurance(accountInsurance.getAccountInsuranceId());
        // when
        accountInsuranceService.deleteAccountInsurance(account.getAccountId(), request);

        em.flush();
        em.clear();
        // then
        Optional<AccountInsurance> assertAccountInsurance = accountInsuranceRepository.findById(accountInsurance.getAccountInsuranceId());
        assertThat(assertAccountInsurance.isEmpty()).isTrue();

    }

    @DisplayName("내 보험 수정 API_성공")
    @Test
    void 내_보험_수정_API_성공() {
        // given
        Account account = accountRepository.save(getAccountBuild("test5@test.com", "테스트5"));
        AccountInsurance accountInsurance = accountInsuranceRepository.save(AccountInsurance.builder()
                .account(account)
                .insuranceType(LF)
                .insuranceCompany("하나보험")
                .build());

        AccountRequest.UpdateAccountInsurance request = new AccountRequest.UpdateAccountInsurance(accountInsurance.getAccountInsuranceId(), accountInsurance.getInsuranceType(), "NH농협생명");

        // when
        accountInsuranceService.updateAccountInsurance(account.getAccountId(), request);
        em.flush();
        em.clear();

        // then
        AccountInsurance assertData = accountInsuranceRepository.findById(accountInsurance.getAccountInsuranceId()).get();
        assertThat(assertData.getInsuranceCompany()).isEqualTo("NH농협생명");

    }

    private static Account getAccountBuild(String email, String nickname) {
        return Account.builder()
                .email(email)
                .nickName(nickname)
                .password("test1234")
                .authority(ROLE_USER)
                .build();
    }
}
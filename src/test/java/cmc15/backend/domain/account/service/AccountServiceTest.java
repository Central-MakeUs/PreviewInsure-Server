package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.request.AccountRequest;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import static cmc15.backend.domain.account.entity.Authority.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("나이 입력 API_성공")
    @Test
    void 나이_입력_API_성공() {
        // given
        Account account = accountRepository.save(getAccountBuild("test@test.com", "불편한 코끼리"));

        AccountRequest.Age request = new AccountRequest.Age(2024, 10);

        // when
        accountService.updateAge(account.getAccountId(), request);

        // then
        LocalDate birthDate = LocalDate.of(request.getYear(), request.getMonth(), 1);
        LocalDate currentDate = LocalDate.now();
        int years = Period.between(birthDate, currentDate).getYears();

        assertThat(account.getAge()).isEqualTo(years);
    }

    @DisplayName("회원탈퇴_API_성공")
    @Test
    void 회원탈퇴_API_성공() {
        // given
        Account account = accountRepository.save(getAccountBuild("test2@test.com", "배고픈 원숭이"));

        // when
        accountService.deleteAccount(account.getAccountId());
        em.flush();
        em.clear();

        // then
        Optional<Account> assertAccount = accountRepository.findById(account.getAccountId());
        assertThat(assertAccount.isEmpty()).isTrue();
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
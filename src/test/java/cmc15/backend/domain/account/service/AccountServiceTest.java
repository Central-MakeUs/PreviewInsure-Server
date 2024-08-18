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

    @DisplayName("나이 입력 API")
    @Test
    void 나이_입력_API_성공() {
        // given
        Account accountBuild = Account.builder()
                .email("test@test.com")
                .nickName("불편한 코끼리")
                .password("test1234")
                .authority(ROLE_USER)
                .build();

        Account account = accountRepository.save(accountBuild);

        AccountRequest.Age request = new AccountRequest.Age(2024, 10);

        // when
        accountService.updateAge(account.getAccountId(), request);

        // then
        LocalDate birthDate = LocalDate.of(request.getYear(), request.getMonth(), 1);
        LocalDate currentDate = LocalDate.now();
        int years = Period.between(birthDate, currentDate).getYears();

        assertThat(account.getAge()).isEqualTo(years);
    }
}
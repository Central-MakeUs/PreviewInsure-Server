package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

    @DisplayName("유저가 회원가입을 진행한다.")
    @Test
    void 회원가입_테스트() {
        // given
        AccountRequest.Register request = new AccountRequest.Register("김덕배", "reviewinsue@gmail.com", "abc123");

        // when
        AccountResponse.Connection response = accountService.accountRegister(request);

        // then
        assertThat(response)
                .extracting("nickName", "email")
                .contains("김덕배", "reviewinsue@gmail.com");

        assertThat(response.getAtk()).isNotNull();
        assertThat(response.getRtk()).isNotNull();
    }

    @DisplayName("유저가 나이를 입력한다.")
    @Test
    void 나이_입력_테스트() {
        // given
        Account account = Account.builder()
                .email("test@test.com")
                .password("abc123")
                .nickName("물만난 물고기")
                .authority(ROLE_USER)
                .build();

        accountRepository.save(account);

        // when
        accountService.updateAge(account.getAccountId(), new AccountRequest.Age(2000, 8));

        // then
        em.flush();
        em.clear();

        Account saveAccount = accountRepository.findById(account.getAccountId()).get();
        assertThat(saveAccount.getAge()).isNotNull();
    }
}
package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @DisplayName("유저가 회원가입을 진행한다.")
    @Test
    void 회원가입_테스트() {
        // given
        AccountRequest.Register request = new AccountRequest.Register("리뷰인슈", "김덕배", "reviewinsue@gmail.com", "abc123");

        // when
        AccountResponse.Connection response = accountService.accountRegister(request);

        // then
        assertThat(response)
                .extracting("name", "nickName", "email")
                .contains("리뷰인슈", "김덕배", "reviewinsue@gmail.com");

        assertThat(response.getAtk()).isNotNull();
        assertThat(response.getRtk()).isNotNull();
    }
}
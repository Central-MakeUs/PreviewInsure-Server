package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.FavoriteInsurance;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.repository.FavoriteInsuranceRepository;
import cmc15.backend.domain.account.request.FavoriteInsuranceRequest;
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
class FavoriteInsuranceServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FavoriteInsuranceRepository favoriteInsuranceRepository;

    @Autowired
    private FavoriteInsuranceService favoriteInsuranceService;

    @Autowired
    private EntityManager em;

    @DisplayName("내 관심 보험 취소 API")
    @Test
    void 내_관심_보험_취소_API() {
        // given
        Account account = accountRepository.save(getAccountBuild("test4@test.com", "test4"));
        FavoriteInsurance favoriteInsurance = favoriteInsuranceRepository.save(FavoriteInsurance.builder()
                .account(account)
                .insuranceType(LF)
                .build());

        FavoriteInsuranceRequest.Delete request = new FavoriteInsuranceRequest.Delete(favoriteInsurance.getFavoriteInsuranceId());

        // when
        favoriteInsuranceService.deleteFavoriteInsurance(account.getAccountId(), request);
        em.flush();
        em.clear();

        // then
        Optional<FavoriteInsurance> assertData = favoriteInsuranceRepository.findById(favoriteInsurance.getFavoriteInsuranceId());
        assertThat(assertData.isEmpty()).isTrue();
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
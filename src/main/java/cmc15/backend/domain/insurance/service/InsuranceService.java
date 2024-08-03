package cmc15.backend.domain.insurance.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.AccountInsurance;
import cmc15.backend.domain.account.entity.InsuranceType;
import cmc15.backend.domain.account.repository.AccountInsuranceRepository;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.insurance.entity.Insurance;
import cmc15.backend.domain.insurance.repository.InsuranceRepository;
import cmc15.backend.domain.insurance.response.InsuranceResponse;
import cmc15.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cmc15.backend.global.Result.NOT_FOUND_USER;
import static java.util.Objects.requireNonNullElse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class InsuranceService {

    public static final String DEFAULT_GENDER = "M";
    public static final long ACCOUNT_INSURANCE_DEFAULT_ID = -1L;
    private final AccountRepository accountRepository;
    private final AccountInsuranceRepository accountInsuranceRepository;
    private final InsuranceRepository insuranceRepository;

    /**
     * @return InsuranceResponse.MapDetail
     * @apiNote 보험 타입 추천 API
     */
    public InsuranceResponse.MapDetail insuranceRecommend(final Long accountId, final InsuranceType insuranceType) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        AccountInsurance accountInsurance = accountInsuranceRepository.findByAccountAndInsuranceType(account, insuranceType).orElse(AccountInsurance.builder().accountInsuranceId(ACCOUNT_INSURANCE_DEFAULT_ID).build());
        List<Insurance> insuranceRecommends = insuranceRepository.findRandomByInsuranceType(insuranceType.name());

        String gender = account.getGender();
        return findGenderRecommend(account, accountInsurance, insuranceRecommends, requireNonNullElse(gender, DEFAULT_GENDER));
    }

    private static InsuranceResponse.MapDetail findGenderRecommend(
            final Account account,
            final AccountInsurance accountInsurance,
            final List<Insurance> insuranceRecommends,
            final String gender
    ) {
        List<InsuranceResponse.Recommend> recommends = insuranceRecommends.stream().map(insurance -> InsuranceResponse.Recommend.to(insurance, gender)).toList();
        return InsuranceResponse.MapDetail.to(account, accountInsurance, recommends);
    }
}

package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.FavoriteInsurance;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.repository.FavoriteInsuranceRepository;
import cmc15.backend.domain.account.request.FavoriteInsuranceRequest;
import cmc15.backend.domain.account.response.FavoriteInsuranceResponse;
import cmc15.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cmc15.backend.global.Result.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteInsuranceService {

    private final AccountRepository accountRepository;
    private final FavoriteInsuranceRepository favoriteInsuranceRepository;

    /**
     * @param accountId
     * @param request
     * @apiNote 관심보험 등록 API
     */
    @Transactional
    public Void addFavoriteInsurance(final Long accountId, final FavoriteInsuranceRequest.Add request) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        favoriteInsuranceRepository.save(FavoriteInsurance.builder()
                .account(account)
                .insuranceType(request.getInsuranceType())
                .build());

        return null;
    }

    /**
     * @param accountId
     * @apiNote 내 관심보험 전체 조회 API
     */
    public List<FavoriteInsuranceResponse.Detail> readFavoriteInsurances(final Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        List<FavoriteInsurance> favoriteInsurances = favoriteInsuranceRepository.findByAccount(account);

        return favoriteInsurances.stream().map(FavoriteInsuranceResponse.Detail::to).toList();
    }
}

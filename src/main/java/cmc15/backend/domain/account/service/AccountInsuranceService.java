package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.entity.AccountInsurance;
import cmc15.backend.domain.account.repository.AccountInsuranceRepository;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.validator.AccountServiceValidator;
import cmc15.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cmc15.backend.global.Result.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountInsuranceService {

    private final AccountServiceValidator accountServiceValidator;
    private final AccountRepository accountRepository;
    private final AccountInsuranceRepository accountInsuranceRepository;

    /**
     * @return List<AccountResponse.Insurances>
     * @apiNote 내가 가입한 보험 조회 API
     */
    public List<AccountResponse.Insurances> readAccountInsurances(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        List<AccountInsurance> accountInsurances = accountInsuranceRepository.findByAccount(account);

        return accountInsurances.stream().map(AccountResponse.Insurances::to).toList();
    }

    /**
     * @return void
     * @apiNote 인슈보딩 입력 API
     */
    @Transactional
    public Void updateInsureBoarding(final Long accountId, final AccountRequest.InsureBoarding request) {
        accountServiceValidator.validateEmptyGender(request);
        Account account = updateGender(accountId, request);

        accountInsuranceRepository.deleteByAccount(account);

        saveInsureBoard(request, account);
        return null;
    }

    private Account updateGender(Long accountId, AccountRequest.InsureBoarding request) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        account.updateGender(request.getGender());
        return account;
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

    /**
     * @param accountId
     * @param request
     * @apiNote 내가 가입한 보험 취소 API
     */
    @Transactional
    public Void deleteAccountInsurance(Long accountId, AccountRequest.DeleteInsurance request) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        AccountInsurance accountInsurance = accountInsuranceRepository.findById(request.getAccountInsuranceId()).orElseThrow(() -> new CustomException(NOT_FOUND_ACCOUNT_INSURANCE));

        validateDeleteAccount(account, accountInsurance);

        accountInsuranceRepository.delete(accountInsurance);
        return null;
    }

    private static void validateDeleteAccount(Account account, AccountInsurance accountInsurance) {
        if (!account.getAccountId().equals(accountInsurance.getAccount().getAccountId())) {
            throw new CustomException(NOT_MATCHED_ACCOUNT_INSURANCE);
        }
    }

    /**
     *
     * @param accountId
     * @param request
     * @apiNote 내가 가입한 보험 수정 API
     */
    @Transactional
    public Void updateAccountInsurance(
            final Long accountId,
            final AccountRequest.UpdateAccountInsurance request
    ) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new CustomException(NOT_FOUND_USER));
        AccountInsurance accountInsurance = getAccountInsurance(request, account);
        accountInsurance.updateAccountInsurance(request.getInsuranceCompany());
        return null;
    }

    private AccountInsurance getAccountInsurance(AccountRequest.UpdateAccountInsurance request, Account account) {
        return accountInsuranceRepository.findByAccountInsuranceIdAndInsuranceTypeAndAccount(
                request.getAccountInsuranceId(), request.getInsuranceType(), account
        ).orElseThrow(() -> new CustomException(NOT_FOUND_ACCOUNT_INSURANCE));
    }
}

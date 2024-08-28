package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.service.AccountInsuranceService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountInsuranceController {

    private final AccountInsuranceService accountInsuranceService;

    /**
     * @param accountId
     * @param request
     * @return void
     * @apiNote 인슈보딩 입력 API
     */
    @PatchMapping("/register/board")
    public CustomResponseEntity<Void> updateInsureBoarding(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final AccountRequest.InsureBoarding request
    ) {
        return CustomResponseEntity.success(accountInsuranceService.updateInsureBoarding(accountId, request));
    }

    /**
     * @param accountId
     * @return
     * @apiNote 내가 가입한 보험 조회 API
     */
    @GetMapping("/account/insurances")
    public CustomResponseEntity<List<AccountResponse.Insurances>> readAccountInsurances(
            @AuthenticationPrincipal final Long accountId
    ) {
        return CustomResponseEntity.success(accountInsuranceService.readAccountInsurances(accountId));
    }

    /**
     * @param accountId
     * @param request
     * @apiNote 내가 가입한 보험 취소 API
     */
    @DeleteMapping("/account/insurance")
    public CustomResponseEntity<Void> deleteAccountInsurance(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final AccountRequest.DeleteInsurance request
    ) {
        return CustomResponseEntity.success(accountInsuranceService.deleteAccountInsurance(accountId, request));
    }

    /**
     *
     * @param accountId
     * @param request
     * @apiNote 내가 가입한 보험 수정 API
     */
    @PatchMapping("/account/insurance")
    public CustomResponseEntity<Void> updateAccountInsurance(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final AccountRequest.UpdateAccountInsurance request
    ) {
        return CustomResponseEntity.success(accountInsuranceService.updateAccountInsurance(accountId, request));
    }
}

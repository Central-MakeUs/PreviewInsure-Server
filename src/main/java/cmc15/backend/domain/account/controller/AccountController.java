package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.entity.Platform;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.service.AccountService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    /**
     * @param code
     * @param appleToken
     * @return
     * @apiNote 소셜 로그인 API
     */
    @PostMapping("/oauth")
    public CustomResponseEntity<AccountResponse.OAuthConnection> socialLogin(
            @RequestParam final Platform platform,
            @RequestParam final String code,
            @RequestParam(required = false) final String appleToken
    ) {
        return CustomResponseEntity.success(accountService.socialLogin(platform, code, appleToken));
    }

    // 회원가입 API
    @PostMapping("/account")
    public CustomResponseEntity<AccountResponse.Connection> accountRegister(
            @RequestBody @Valid final AccountRequest.Register request
    ) {
        return CustomResponseEntity.success(accountService.accountRegister(request));
    }

    /**
     * @return success
     * @apiNote 랜덤 닉네임 생성 API
     */
    @GetMapping("/register/nickname")
    public CustomResponseEntity<AccountResponse.NickName> createNickName() {
        return CustomResponseEntity.success(accountService.createNickName());
    }

    /**
     * @param accountId
     * @param request
     * @return void
     * @apiNote 나이 입력 API
     */
    @PatchMapping("/register/age")
    public CustomResponseEntity<Void> updateAge(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final AccountRequest.Age request
    ) {
        return CustomResponseEntity.success(accountService.updateAge(accountId, request));
    }

    /**
     * @param accountId
     * @param request
     * @return void
     * @apiNote 인슈보딩 입력 API
     */
    @PatchMapping("/register/board")
    public CustomResponseEntity<Void> updateInsureBoarding(
            @AuthenticationPrincipal Long accountId,
            @RequestBody @Valid final AccountRequest.InsureBoarding request
    ) {
        return CustomResponseEntity.success(accountService.updateInsureBoarding(accountId, request));
    }

    /**
     * @param accountId
     * @return
     * @apiNote 내가 가입한 보험 조회 API
     */
    @GetMapping("/account/insurances")
    public CustomResponseEntity<List<AccountResponse.Insurances>> readAccountInsurances(
            @AuthenticationPrincipal Long accountId
    ) {
        return CustomResponseEntity.success(accountService.readAccountInsurances(accountId));
    }
}

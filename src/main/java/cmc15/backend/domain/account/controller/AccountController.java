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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

    /**
     * @param platform
     * @param code
     * @apiNote 소셜 로그인 API
     */
    @GetMapping("/oauth")
    public CustomResponseEntity<AccountResponse.OAuthConnection> socialLogin(
            @RequestParam final Platform platform,
            @RequestParam final String code
    ) {
        return CustomResponseEntity.success(accountService.socialLogin(platform, code));
    }

    /**
     * @apiNote 랜덤 닉네임 생성 API
     */
    @GetMapping("/register/nickname")
    public CustomResponseEntity<AccountResponse.NickName> createNickName() {
        return CustomResponseEntity.success(accountService.createNickName());
    }

    /**
     * @param accountId
     * @param request
     * @apiNote 닉네임 업데이트 API
     */
    @PatchMapping("register/nickname")
    public CustomResponseEntity<Void> updateNickname(
            @AuthenticationPrincipal final Long accountId,
            @RequestBody @Valid final AccountRequest.Nickname request
    ) {
        return CustomResponseEntity.success(accountService.updateNickname(accountId, request));
    }

    /**
     * @param accountId
     * @param request
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
     * @apiNote 회원탈퇴 API
     */
    @DeleteMapping("/account")
    public CustomResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal final Long accountId
    ) {
        return CustomResponseEntity.success(accountService.deleteAccount(accountId));
    }

    /**
     * @param accountId
     * @apiNote 내 정보 조회 API
     */
    @GetMapping("/account")
    public CustomResponseEntity<AccountResponse.Detail> readAccount(
            @AuthenticationPrincipal final Long accountId
    ) {
        return CustomResponseEntity.success(accountService.readAccount(accountId));
    }
}

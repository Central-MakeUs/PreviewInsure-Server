package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.service.AccountService;
import cmc15.backend.global.CustomResponseEntity;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final AccountService accountService;

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

    @PostMapping("/callback/apple")
    public String test(
            @RequestBody MultiValueMap<String, Object> request
    ) {
        // 전달 받은 data에서 token 값 저장
        String id_token = request.get("id_token").toString();
        String email = "";
        try {
            //token값 decode처리
            SignedJWT signedJWT = SignedJWT.parse(id_token);
            //token값에서 payload 저장
            ReadOnlyJWTClaimsSet payload = signedJWT.getJWTClaimsSet();
            //payload에서 email 값 저장
            email = payload.getClaim("email").toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/https://preview-insure-web-git-dev-sehuns-projects.vercel.app/callback/apple?token=" + email;
    }
}

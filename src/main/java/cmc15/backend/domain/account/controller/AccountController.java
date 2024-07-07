package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.service.AccountService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

}

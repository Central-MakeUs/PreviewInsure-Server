package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.service.AccountService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppleController {

    private final AccountService accountService;

    /**
     * @apiNote 애플 로그인 API
     * @param request
     * @return AccountResponse.OAuthConnection
     */
    @PostMapping("/apple/token")
    public CustomResponseEntity<AccountResponse.OAuthConnection> appleLogin(HttpServletRequest request) {
        return CustomResponseEntity.success(accountService.getAppleInfo(request.getParameter("code")));
    }
}

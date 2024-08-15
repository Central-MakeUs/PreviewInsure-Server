package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.domain.account.service.AccountService;
import cmc15.backend.global.CustomResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AppleController {

    private final AccountService accountService;

    /**
     * @param request
     * @return AccountResponse.OAuthConnection
     * @apiNote 애플 로그인 API
     */
    @PostMapping("/apple/token")
    public void appleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CustomResponseEntity<AccountResponse.OAuthConnection> code = CustomResponseEntity.success(accountService.getAppleInfo(request.getParameter("code")));
        response.sendRedirect("https://preview-insure-web-git-dev-sehuns-projects.vercel.app/callback/apple?token=" + code.getData().getAtk());
    }
}

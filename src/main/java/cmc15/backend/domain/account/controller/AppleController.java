package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static cmc15.backend.domain.account.entity.Platform.APPLE;

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
    public void appleLogin2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getParameter("code"));
        response.sendRedirect(accountService.socialLogin(APPLE, request.getParameter("code")).getRedirectUrl());
    }
}

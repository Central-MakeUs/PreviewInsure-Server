package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.service.AccountService;
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
        response.sendRedirect(accountService.appleLogin(request.getParameter("code")));
    }
}

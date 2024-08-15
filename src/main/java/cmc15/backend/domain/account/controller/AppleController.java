package cmc15.backend.domain.account.controller;

import cmc15.backend.domain.account.request.AppleDTO;
import cmc15.backend.domain.account.response.MsgEntity;
import cmc15.backend.domain.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppleController {
    private final AccountService accountService;

    @PostMapping("/apple/token")
    public ResponseEntity<MsgEntity> callback(HttpServletRequest request) throws Exception {
        AppleDTO appleInfo = accountService.getAppleInfo(request.getParameter("code"));
        System.out.println(appleInfo.getEmail());
        return ResponseEntity.ok()
                .body(new MsgEntity("Success", appleInfo));
    }
}

package cmc15.backend.domain.account.service;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.domain.account.request.AccountRequest;
import cmc15.backend.domain.account.response.AccountResponse;
import cmc15.backend.global.config.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cmc15.backend.domain.account.entity.Authority.ROLE_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입 API
    @Transactional
    public AccountResponse.Connection accountRegister(final AccountRequest.Register request) {
        // 계정 등록
        Account account = saveAccount(request);
        // 토큰 발급
        String atk = tokenProvider.createAccessToken(account.getAccountId(), getAuthentication(account.getEmail(), request.getPassword()));
        String rtk = tokenProvider.createRefreshToken(account.getEmail());

        return AccountResponse.Connection.to(account, atk, rtk);
    }

    private Account saveAccount(AccountRequest.Register request) {
        return accountRepository.save(Account.builder()
                .name(request.getName())
                .nickName(request.getNickName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .authority(ROLE_USER)
                .build());
    }

    private Authentication getAuthentication(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}

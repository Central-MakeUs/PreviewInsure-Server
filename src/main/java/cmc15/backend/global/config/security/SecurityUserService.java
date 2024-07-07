package cmc15.backend.global.config.security;

import cmc15.backend.domain.account.entity.Account;
import cmc15.backend.domain.account.repository.AccountRepository;
import cmc15.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cmc15.backend.global.Result.NOT_FOUND_USER;

@Service
@Component("userDetailsService")
@RequiredArgsConstructor
public class SecurityUserService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) {
        return accountRepository.findByEmail(email)
                .map(this::createUser)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }

    private User createUser(Account account) {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(account.getAuthority().getAuthority());
        List<GrantedAuthority> grantedAuthorities = List.of(simpleGrantedAuthority);
        return new User(account.getEmail(), account.getPassword(), grantedAuthorities);
    }
}

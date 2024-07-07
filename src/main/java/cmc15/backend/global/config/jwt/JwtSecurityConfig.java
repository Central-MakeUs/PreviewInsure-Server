package cmc15.backend.global.config.jwt;

import cmc15.backend.global.config.redis.RedisDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final RedisDao redisDao;

    @Override
    public void configure(HttpSecurity http) {
        http.addFilterBefore(new JwtFilter(tokenProvider, redisDao), UsernamePasswordAuthenticationFilter.class);
    }
}

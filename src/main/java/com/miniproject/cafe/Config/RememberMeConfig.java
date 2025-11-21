package com.miniproject.cafe.Config;

import com.miniproject.cafe.Service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@RequiredArgsConstructor
public class RememberMeConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public RememberMeServices rememberMeServices() {

        TokenBasedRememberMeServices rememberMeServices =
                new TokenBasedRememberMeServices("secure-key", customUserDetailsService);

        rememberMeServices.setAlwaysRemember(true);   // OAuth 로그인도 자동로그인 ON
        rememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 14);  // 14일
        rememberMeServices.setParameter("remember-me");

        return rememberMeServices;
    }
}
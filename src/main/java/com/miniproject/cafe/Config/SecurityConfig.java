package com.miniproject.cafe.Config;

import com.miniproject.cafe.Handler.FormLoginFailureHandler;
import com.miniproject.cafe.Handler.OAuth2FailureHandler;
import com.miniproject.cafe.Handler.FormLoginSuccessHandler;
import com.miniproject.cafe.Handler.OAuthLoginSuccessHandler;
import com.miniproject.cafe.Service.CustomOAuth2UserService;
import com.miniproject.cafe.Service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final FormLoginFailureHandler formLoginFailureHandler;
    private final FormLoginSuccessHandler formLoginSuccessHandler;
    private final OAuthLoginSuccessHandler oAuthLoginSuccessHandler;
    private final RememberMeServices rememberMeServices;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .headers(h -> h.frameOptions(f -> f.sameOrigin()))

                .authorizeHttpRequests(auth -> auth
                        // [핵심] "/", "/home" 둘 다 명확하게 허용 (슬래시 주의)
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/api/member/**", "/login/oauth2/**").permitAll()
                        .requestMatchers("/menu/**").permitAll()
                        .requestMatchers("/home/saveRegion", "/home/getRegion").permitAll()
                        .requestMatchers("/home/login").permitAll()
                        // 로그인 필요 페이지들
                        .requestMatchers("/home/**").authenticated()

                        .anyRequest().permitAll()
                )

                .formLogin(f -> f
                        // [핵심] 로그인 페이지를 루트("/")로 설정하여 경로 충돌 방지
                        .loginPage("/home/login")
                        .loginProcessingUrl("/login")
                        .successHandler(formLoginSuccessHandler)
                        .failureHandler(formLoginFailureHandler)
                        .permitAll()
                )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/home/login")     // 반드시 /home 으로 고정
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oAuthLoginSuccessHandler)
                        .failureHandler(oAuth2FailureHandler)
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/home/login") // 로그아웃 후에도 루트로
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                )

                .rememberMe(r -> r
                        .rememberMeServices(rememberMeServices)
                );

        return http.build();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
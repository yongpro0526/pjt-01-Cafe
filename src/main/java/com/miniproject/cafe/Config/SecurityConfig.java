package com.miniproject.cafe.Config;

import com.miniproject.cafe.Service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home/**", "/menu/**",
                                "/css/**", "/js/**", "/images/**",
                                "/api/**", "/error", "/oauth2/**"
                        ).permitAll()

                        // 🔥 admin 전체 허용
                        .requestMatchers("/admin/**").permitAll()

                        .anyRequest().permitAll()
                )

                // 🔥 formLogin을 네가 API 로그인 방식으로 쓰고 있으므로 사실상 의미 없음
                .formLogin(login -> login
                        .loginPage("/login")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
                )

                // 🔥 OAuth2 로그인
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .userInfoEndpoint(info -> info.userService(customOAuth2UserService))
                        .defaultSuccessUrl("/home/", true)
                );

        return http.build();
    }
}
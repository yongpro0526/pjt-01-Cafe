package com.miniproject.cafe.config;

import com.miniproject.cafe.Handler.CustomOAuth2FailureHandler;
import com.miniproject.cafe.Service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // final 필드 주입을 위해 (Lombok)
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Saved Request를 무시하는 SuccessHandler 객체 생성
        SimpleUrlAuthenticationSuccessHandler successHandler =
                new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setAlwaysUseDefaultTargetUrl(true);
        successHandler.setDefaultTargetUrl("/home/");

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login", "/js/**", "/css/**", "/images/**", "/.well-known/**").permitAll()

                        // ⭐ 홈 화면 전체 접근 허용
                        .requestMatchers("/home/**").permitAll()

                        // ⭐ 메뉴 화면 전체 접근 허용
                        .requestMatchers("/menu/**").permitAll()

                        // ⭐ 회원가입/로그인 API 접근 허용
                        .requestMatchers("/api/member/**").permitAll()

                        // 관리자 페이지
                        .requestMatchers("/admin/**").permitAll()

                        // 그 외는 인증 필요
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/home/")
                        .permitAll()
                )

                // --- ✨ OAuth 2.0 (소셜 로그인) 설정 ---
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                        .successHandler(successHandler)
                        .failureHandler(customOAuth2FailureHandler)
                );

        return http.build();
    }
}
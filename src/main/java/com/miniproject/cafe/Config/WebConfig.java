package com.miniproject.cafe.Config;

import com.miniproject.cafe.Interceptor.RememberMeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RememberMeInterceptor rememberMeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(rememberMeInterceptor)
                .addPathPatterns("/**")   // 전체 URL 자동로그인 적용
                .excludePathPatterns(
                        "/css/**", "/js/**", "/images/**",
                        "/api/member/login",
                        "/api/member/signup",
                        "/logout",
                        "/error"
                );
    }
}

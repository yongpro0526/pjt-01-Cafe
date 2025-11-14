package com.miniproject.cafe.Interceptor;

import com.miniproject.cafe.Mapper.MemberMapper;
import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class RememberMeInterceptor implements HandlerInterceptor {

    private final MemberMapper memberMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        // 이미 로그인 되어 있으면 통과
        if (session != null && session.getAttribute("loginMember") != null) {
            return true;
        }

        // 쿠키 읽기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return true;

        String token = null;
        for (Cookie cookie : cookies) {
            if ("remember-me".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null) return true;

        // DB에서 토큰 검증
        MemberVO member = memberMapper.findByRememberMeToken(token);

        if (member != null) {
            // 자동 로그인 성공 → 세션 저장
            HttpSession newSession = request.getSession();
            newSession.setAttribute("loginMember", member);

            // 토큰 재발급 (Sliding Session 방식)
            Cookie newCookie = new Cookie("remember-me", token);
            newCookie.setPath("/");
            newCookie.setHttpOnly(true);
            newCookie.setMaxAge(14 * 24 * 60 * 60);
            response.addCookie(newCookie);
        }

        return true;
    }
}

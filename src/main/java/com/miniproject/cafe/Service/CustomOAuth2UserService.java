package com.miniproject.cafe.Service;

import com.miniproject.cafe.Mapper.MemberMapper;
import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession httpSession;
    private final HttpServletResponse response; // 자동로그인 쿠키 생성용

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
                new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email;
        String username;

        Map<String, Object> attributes = oAuth2User.getAttributes();

        /** Provider별 파싱 */
        switch (provider) {
            case "naver":
                Map<String, Object> res = (Map<String, Object>) attributes.get("response");
                email = (String) res.get("email");
                username = (String) res.get("name");
                break;

            case "kakao":
                Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) account.get("profile");
                email = (String) account.get("email");
                username = (String) profile.get("nickname");
                break;

            default: // google
                email = (String) attributes.get("email");
                username = (String) attributes.get("name");
        }

        MemberVO found = memberMapper.findByEmail(email);
        MemberVO member;

        if (found != null) {
            member = found;
            String existingProvider = member.getProvider();

            // 일반 계정이면 소셜 로그인 불가
            if (existingProvider == null || "general".equals(existingProvider)) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("LOCAL_ACCOUNT"),
                        "이미 일반로그인으로 가입된 이메일입니다."
                );
            }

            // provider가 다르면 충돌
            if (!existingProvider.equals(provider)) {
                throw new OAuth2AuthenticationException(
                        new OAuth2Error("PROVIDER_CONFLICT"),
                        "이미 " + existingProvider + " 계정으로 가입된 이메일입니다."
                );
            }

            // 이름 변경되었으면 업데이트
            if (!member.getUsername().equals(username)) {
                member.setUsername(username);
                memberMapper.updateUser(member);
            }

            httpSession.setAttribute("loginSuccessType", "LOGIN");
            httpSession.setAttribute("loginMemberName", member.getUsername());

        } else {
            // 신규 회원가입
            member = new MemberVO();
            member.setEmail(email);
            member.setId(email);
            member.setUsername(username);
            member.setProvider(provider);

            String dummyPw = UUID.randomUUID().toString();
            member.setPassword(passwordEncoder.encode(dummyPw));

            memberMapper.insertOAuthMember(member);

            httpSession.setAttribute("loginSuccessType", "SIGNUP");
            httpSession.setAttribute("loginMemberName", member.getUsername());
        }

        // 세션 로그인 처리
        httpSession.setAttribute("loginMember", member);

        /** ⭐ SNS 로그인도 자동로그인 적용 — remember-me 쿠키 발급 */
        saveRememberMeToken(member.getId());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes,
                userRequest.getClientRegistration()
                        .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
        );
    }

    /** SNS 자동 로그인 쿠키 발급 */
    private void saveRememberMeToken(String memberId) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusDays(14);

        memberMapper.saveRememberMeToken(memberId, token, expiry);

        Cookie cookie = new Cookie("remember-me", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(14 * 24 * 60 * 60);

        response.addCookie(cookie);
    }
}

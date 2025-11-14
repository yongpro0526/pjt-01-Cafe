package com.miniproject.cafe.Impl;

import com.miniproject.cafe.Mapper.MemberMapper;
import com.miniproject.cafe.Service.MemberService;
import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String registerMember(MemberVO vo) {
        if (!vo.getPassword().equals(vo.getPasswordCheck())) {
            return "PASSWORD_MISMATCH";
        }

        if (memberMapper.isIdDuplicate(vo.getId())) {
            return "ID_DUPLICATE";
        }

        if (memberMapper.isEmailDuplicate(vo.getEmail())) {
            return "EMAIL_DUPLICATE";
        }

        vo.setPassword(passwordEncoder.encode(vo.getPassword()));
        vo.setProvider("general");

        memberMapper.registerMember(vo);
        return "SUCCESS";
    }

    @Override
    public boolean loginMember(MemberVO vo, HttpSession session) {
        MemberVO storedMember = memberMapper.loginMember(vo);

        if (storedMember != null &&
                passwordEncoder.matches(vo.getPassword(), storedMember.getPassword())) {

            session.setAttribute("loginMember", storedMember);
            return true;
        }
        return false;
    }

    @Override
    public boolean isIdDuplicate(String id) {
        return memberMapper.isIdDuplicate(id);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return memberMapper.isEmailDuplicate(email);
    }

    // ⭐ 추가: 일반 로그인
    @Override
    public MemberVO login(String email, String password) {
        MemberVO member = memberMapper.findByEmail(email);
        if (member == null) return null;

        if (!passwordEncoder.matches(password, member.getPassword())) {
            return null;
        }
        return member;
    }

    // ⭐ 추가: 자동로그인 토큰 저장 + 쿠키 생성
    @Override
    public void saveRememberMeToken(String memberId, HttpServletResponse response) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusDays(14);

        memberMapper.saveRememberMeToken(memberId, token, expiry);

        Cookie cookie = new Cookie("remember-me", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(14 * 24 * 60 * 60); // 14일

        response.addCookie(cookie);
    }
}

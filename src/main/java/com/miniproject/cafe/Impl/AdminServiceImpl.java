package com.miniproject.cafe.Impl;

import com.miniproject.cafe.Mapper.AdminMapper;
import com.miniproject.cafe.Service.AdminService;
import com.miniproject.cafe.VO.AdminVO;
import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Autowired
    private final AdminMapper adminMapper;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String registerMember(MemberVO vo) {
        if (vo.getPassword() == null || vo.getPasswordCheck() == null || !vo.getPassword().equals(vo.getPasswordCheck())) {
            return "PASSWORD_MISMATCH";
        }

        if (AdminMapper.isIdDuplicate(vo.getId())) {
            return "ID_DUPLICATE";
        }

        if (AdminMapper.isEmailDuplicate(vo.getEmail())) {
            return "EMAIL_DUPLICATE";
        }
        String rawPassword = vo.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        vo.setPassword(encodedPassword);
        vo.setProvider("general");

        AdminMapper.registerMember(vo);
        return "SUCCESS";
    }

    @Override
    public boolean loginMember(MemberVO vo, HttpSession session) {
        MemberVO storedMember = AdminMapper.loginMember(vo);

        if (storedMember != null) {
            // 사용자가 입력한 평문 비밀번호와 DB에 저장된 암호화된 비밀번호를 비교
            if (passwordEncoder.matches(vo.getPassword(), storedMember.getPassword())) {
                session.setAttribute("loginMember", storedMember); // 비밀번호 일치 (로그인 성공)
                return true;
            }
        }
        return false; //비밀번호 불일치 또는 회원 없음
    }

    @Override
    public boolean isIdDuplicate(String id) {
        return AdminMapper.isIdDuplicate(id);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return AdminMapper.isEmailDuplicate(email);
    }

}

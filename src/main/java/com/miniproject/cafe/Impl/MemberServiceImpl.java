package com.miniproject.cafe.Impl;

import com.miniproject.cafe.Mapper.MemberMapper;
import com.miniproject.cafe.Service.MemberService;
import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    // @Autowired
    // private PasswordEncoder passwordEncoder; // (삭제)

    @Override
    @Transactional
    public String registerMember(MemberVO vo) {

        if (memberMapper.isIdDuplicate(vo.getId())) {
            return "ID_DUPLICATE";
        }

        if (memberMapper.isEmailDuplicate(vo.getEmail())) {
            return "EMAIL_DUPLICATE";
        }
        memberMapper.registerMember(vo);
        return "SUCCESS";
    }

    @Override
    public boolean loginMember(MemberVO vo, HttpSession session) {
        MemberVO foundMember = memberMapper.loginMember(vo);

        if (foundMember != null) {
            session.setAttribute("loginMember", foundMember);
            return true;
        } else {
            return false; // ID 또는 비밀번호 불일치
        }
    }

    @Override
    public boolean isIdDuplicate(String id) {
        return memberMapper.isIdDuplicate(id);
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        return memberMapper.isEmailDuplicate(email);
    }
}
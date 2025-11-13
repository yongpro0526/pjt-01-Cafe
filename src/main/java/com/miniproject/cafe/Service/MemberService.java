package com.miniproject.cafe.Service;

import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.http.HttpSession;

public interface MemberService {
    String registerMember(MemberVO vo); //성공 시 "SUCCESS", 아이디 중복 시 "ID_DUPLICATE", 이메일 중복 시 "EMAIL_DUPLICATE"
    boolean loginMember(MemberVO vo, HttpSession session); //로그인 처리 성공시 true, 실패시 false
    boolean isIdDuplicate(String id); //id 중복확인 용도
    boolean isEmailDuplicate(String email); //이메일 중복확인 용도
}
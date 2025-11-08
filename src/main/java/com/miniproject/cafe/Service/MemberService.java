package com.miniproject.cafe.Service;

import com.miniproject.cafe.VO.MemberVO;

public interface MemberService {
    MemberVO login(String uId, String uPw); //로그인 성공시 MemberVO 반환
    int register(MemberVO member); //회원가입 성공시 1반환
    MemberVO getMemberById(String uId); //회원정보 조회 (아이디 중복체크 기능)
}


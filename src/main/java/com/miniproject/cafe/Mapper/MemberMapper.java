package com.miniproject.cafe.Mapper;

import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {
    void registerMember(MemberVO vo); //성공 시 "SUCCESS", 아이디 중복 시 "ID_DUPLICATE", 이메일 중복 시 "EMAIL_DUPLICATE"
    MemberVO loginMember(MemberVO vo); //ID와 Password가 일치하는 회원을 조회
    boolean isIdDuplicate(String id); //id 중복확인 용도
    boolean isEmailDuplicate(String email); //이메일 중복확인 용도
}
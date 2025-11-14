package com.miniproject.cafe.Mapper;

import com.miniproject.cafe.VO.MemberVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface MemberMapper {

    // 일반 회원가입
    void registerMember(MemberVO vo);

    // 일반 로그인
    MemberVO loginMember(MemberVO vo);

    // 중복 검사
    boolean isIdDuplicate(String id);
    boolean isEmailDuplicate(String email);

    // 이메일로 회원 조회 (sns 포함)
    MemberVO findByEmail(String email);

    // OAuth(네이버/카카오/구글) 신규 회원 등록
    void insertOAuthMember(MemberVO vo);

    // 사용자 정보 업데이트 (이름 변경 등)
    void updateUser(MemberVO vo);

    // 자동로그인 토큰 저장
    void saveRememberMeToken(@Param("memberId") String memberId,
                             @Param("token") String token,
                             @Param("expiry") LocalDateTime expiry);

    // 자동로그인 토큰으로 회원 조회
    MemberVO findByRememberMeToken(String token);

    // 로그아웃 시 토큰 삭제
    void clearRememberMeToken(String memberId);
}

package com.miniproject.cafe.Mapper;

import com.miniproject.cafe.VO.MemberVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

    MemberVO findMemberById(String uId); //ID로 회원 조회
    int insertMember(MemberVO member); //회원 정보 삽입
}
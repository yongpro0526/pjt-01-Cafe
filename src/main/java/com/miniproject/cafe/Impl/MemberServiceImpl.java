package com.miniproject.cafe.Impl;

import com.miniproject.cafe.Mapper.MemberMapper;
import com.miniproject.cafe.Service.MemberService;
import com.miniproject.cafe.VO.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public MemberVO login(String uId, String uPw) {
        // 1. ID로 회원 정보 조회
        MemberVO member = memberMapper.findMemberById(uId);

        // 2. 회원이 존재하고, 비밀번호가 일치하는지 확인
        // (실제로는 uPw를 암호화하여 DB의 암호화된 비밀번호와 비교해야 합니다.)
        if (member != null && member.getUPw().equals(uPw)) {
            return member;
        }

        // 3. 로그인 실패
        return null;
    }

    @Override
    public int register(MemberVO member) {
        // TODO: 비밀번호 암호화 로직 추가

        try {
            return memberMapper.insertMember(member);
        } catch (Exception e) {
            // (예: ID 중복(PK) 오류 등)
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public MemberVO getMemberById(String uId) {
        return memberMapper.findMemberById(uId);
    }
}

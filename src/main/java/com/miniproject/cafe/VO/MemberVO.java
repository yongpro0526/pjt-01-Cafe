package com.miniproject.cafe.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberVO {

    private String uId;       // 사용자 ID (PK)
    private String uPw;       // 사용자 비밀번호
    private String username;  // 사용자 이름
    private String email;     // 사용자 이메일

    // ERD를 기반으로 추가
    private String menuId;
    private Integer orderNum;
}
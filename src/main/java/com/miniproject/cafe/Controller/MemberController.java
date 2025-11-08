package com.miniproject.cafe.Controller;

import com.miniproject.cafe.Service.MemberService;
import com.miniproject.cafe.VO.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/member") // API 경로는 /api/ 로 시작하는 것을 권장
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * 로그인 API
     * @param loginRequest (예: {"uId": "test", "uPw": "1234"})
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<MemberVO> login(@RequestBody Map<String, String> loginRequest) {
        String uId = loginRequest.get("uId");
        String uPw = loginRequest.get("uPw");

        MemberVO member = memberService.login(uId, uPw);

        if (member != null) {
            // 로그인 성공 (HTTP 200 OK)
            member.setUPw(null); // 보안: 비밀번호는 반환하지 않음
            return ResponseEntity.ok(member);
        } else {
            // 로그인 실패 (HTTP 401 Unauthorized)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 회원가입 API
     * @param member (JSON 형태로 MemberVO 데이터를 받음)
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody MemberVO member) {
        int result = memberService.register(member);

        if (result > 0) {
            // 회원가입 성공 (HTTP 201 Created)
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
        } else {
            // 회원가입 실패 (HTTP 500 Internal Server Error)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패 (ID 중복 등)");
        }
    }

    /**
     * 회원 정보 조회 API
     * @param uId (URL 경로에서 ID를 받음)
     * @return
     */
    @GetMapping("/{uId}")
    public ResponseEntity<MemberVO> getMemberInfo(@PathVariable("uId") String uId) {
        MemberVO member = memberService.getMemberById(uId);

        if (member != null) {
            member.setUPw(null); // 보안
            return ResponseEntity.ok(member);
        } else {
            // 회원 없음 (HTTP 404 Not Found)
            return ResponseEntity.notFound().build();
        }
    }
}

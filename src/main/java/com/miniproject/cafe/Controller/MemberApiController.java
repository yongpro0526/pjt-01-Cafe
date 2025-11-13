package com.miniproject.cafe.Controller;


import com.miniproject.cafe.Service.MemberService;
import com.miniproject.cafe.VO.MemberVO;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/member") // 모달창과 통신할 API 경로
public class MemberApiController {

    @Autowired
    private MemberService memberService;

    /* 회원가입 로직 */
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> handleSignup(@RequestBody MemberVO vo) {
        String result = memberService.registerMember(vo);
        System.out.println("--- 회원가입 요청 처리 ---");
        System.out.println("전달받은 데이터: " + vo.toString());
        System.out.println("서비스 결과: " + result);
        return switch (result) {
            case "SUCCESS" -> {
                System.out.println(">>> SUCCESS 분기 실행");
                yield ResponseEntity.ok(Map.of("message", "회원가입 성공! 로그인해주세요."));
            }
            case "ID_DUPLICATE" -> {
                System.out.println(">>> ID_DUPLICATE 분기 실행");
                yield ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "이미 사용 중인 ID입니다."));
            }
            case "EMAIL_DUPLICATE" -> {
                System.out.println(">>> EMAIL_DUPLICATE 분기 실행");
                yield ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "이미 사용 중인 이메일입니다."));
            }
            default -> {
                System.out.println(">>> DEFAULT 분기 실행 (알 수 없는 오류)");
                yield ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "알 수 없는 오류가 발생했습니다."));
            }
        };
    }
    /* 로그인 로직 */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> handleLogin(@RequestBody MemberVO vo, HttpSession session) {

        boolean loginSuccess = memberService.loginMember(vo, session);

        if (loginSuccess) {
            return ResponseEntity.ok(Map.of("message", "로그인 성공!"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "아이디 또는 비밀번호가 일치하지 않습니다."));
        }
    }
}
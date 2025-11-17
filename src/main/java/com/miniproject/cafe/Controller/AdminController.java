package com.miniproject.cafe.Controller;

import com.miniproject.cafe.Service.AdminService;
import com.miniproject.cafe.VO.AdminVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @Autowired
    private AdminService adminService;

    // 개행 문자 제거하는 sanitizing 메서드 추가
    private String sanitize(String msg) {
        if (msg == null) return "";
        return msg.replaceAll("[\r\n]", "");  // CR/LF 제거
    }

    @GetMapping("/orders")
    public String adminOrders(HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        return "admin_orders";
    }

    // 회원가입 화면
    @GetMapping("/signup")
    public String adminSignup() {
        return "admin_signup";
    }

    // 회원가입 처리
    @PostMapping("/joinForm")
    public String signup(AdminVO vo) {
        try {
            adminService.register(vo);
        } catch (RuntimeException e) {

            // 개행 제거 적용
            return "redirect:/admin/signup?error=" + sanitize(e.getMessage());
        }

        return "redirect:/admin/login";
    }

    // 로그인 화면
    @GetMapping("/login")
    public String adminLogin() {
        return "admin_login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@RequestParam String id,
                        @RequestParam String password,
                        HttpSession session) {

        try {
            AdminVO admin = adminService.login(id, password);
            session.setAttribute("adminId", admin.getId());
            session.setAttribute("storeName", admin.getStoreName());

        } catch (RuntimeException e) {

            // 로그인 실패 메시지도 sanitize 적용
            return "redirect:/admin/login?error=" + sanitize(e.getMessage());
        }

        return "redirect:/admin/orders";
    }

    // 아이디 중복 체크 API
    @GetMapping("/checkId")
    @ResponseBody
    public String checkId(@RequestParam String id) {
        int count = adminService.checkId(id);
        return count > 0 ? "duplicate" : "available";
    }
}

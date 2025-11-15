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

    @GetMapping("/orders")
    public String adminOrders(HttpSession session) {
        // 세션 체크 (로그인 여부 확인)
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        return "admin_orders"; // 관리자 주문 페이지
    }

    // 회원가입 화면
    @GetMapping("/signup")
    public String adminSignup() {
        return "admin_signup";  // 기존 뷰 이름 유지
    }

    // 회원가입 처리
    @PostMapping("/joinForm")
    public String signup(AdminVO vo) {
        try {
            adminService.register(vo);
        } catch (RuntimeException e) {
            // 예: 아이디 중복 시 다시 회원가입 페이지로 redirect
            return "redirect:/admin/signup?error=" + e.getMessage();
        }
        return "redirect:/admin/login";
    }


    // 로그인 화면
    @GetMapping("/login")
    public String adminLogin() {
        return "admin_login";  // 기존 뷰 이름 유지
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
            return "redirect:/admin/login?error=" + e.getMessage();
        }

        return "redirect:/admin/orders";
    }

    // 아이디 중복 체크 API
    @GetMapping("/checkId")
    @ResponseBody
    public String checkId(@RequestParam String id) {
        int count = adminService.checkId(id);
        if (count > 0) {
            return "duplicate";
        } else {
            return "available";
        }
    }



}

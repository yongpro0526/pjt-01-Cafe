package com.miniproject.cafe.Controller;

import com.miniproject.cafe.Service.AdminService;
import com.miniproject.cafe.VO.AdminVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private AdminService adminService;

    @GetMapping("/orders")
    public String adminOrders(HttpSession session) {
        // 세션 체크 (로그인 여부 확인)
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        return "admin_orders"; // 관리자 주문 페이지
    }

    @GetMapping("/signup")
    public String adminSignup() {
        return "admin_signup";
    }

    @GetMapping("/login")
    public String adminLogin() {
        return "admin_login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String id,
                        @RequestParam String password,
                        HttpSession session) {

        AdminVO admin = new AdminVO();
        admin.setId(id);
        admin.setPassword(password);

        AdminVO loginAdmin = adminService.login(admin);

        if (loginAdmin == null) {
            // 로그인 실패 → 로그인 페이지로 다시 이동, error 쿼리 추가
            return "redirect:/admin/login?error=true";
        }

        // 로그인 성공 → 세션에 저장
        session.setAttribute("adminId", loginAdmin.getId());

        // 로그인 성공 후 이동
        return "redirect:/admin/orders";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @PostMapping("/joinForm")
    public String joinForm(@RequestParam String id,
                           @RequestParam String password,
                           @RequestParam String storeName,
                           Model model) {

        // 아이디 중복 체크
        if(adminService.isIdDuplicate(id)) {
            model.addAttribute("errorMessage", "이미 존재하는 아이디입니다.");
            return "admin_signup"; // 다시 회원가입 페이지로
        }

        // AdminVO 생성
        AdminVO admin = new AdminVO();
        admin.setId(id);
        admin.setPassword(password);
        admin.setStoreName(storeName);

        // 회원가입 처리
        adminService.signup(admin);

        // 가입 성공 후 로그인 페이지로 이동
        return "redirect:/admin/login";
    }

}

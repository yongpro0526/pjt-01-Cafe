package com.miniproject.cafe.Controller;

import com.miniproject.cafe.VO.MenuVO;
import com.miniproject.cafe.Service.UserLikeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final UserLikeService userLikeService;

    @GetMapping("/")
    public String home(Model model, Authentication auth) {
        boolean isLoggedIn = (auth != null && auth.isAuthenticated());
        model.addAttribute("IS_LOGGED_IN", isLoggedIn);
        return "main";
    }

    @GetMapping("/order_history")
    public String order_history() {
        return "order_history";
    }

    @GetMapping("/coffee")
    public String food() {
        return "redirect:/menu/coffee";
    }

    @GetMapping("/mypick")
    public String myPickPage(Model model, Authentication auth) {

        if(auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String userId = auth.getName();
        List<MenuVO> likedMenus = userLikeService.getLikedMenus(userId);

        model.addAttribute("likedMenus", likedMenus);

        return "mypick";
    }
}

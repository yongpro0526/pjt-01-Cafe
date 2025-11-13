package com.miniproject.cafe.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/")
    public String home() {
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
    public String myPickPage() {
        return "mypick";
    }
}

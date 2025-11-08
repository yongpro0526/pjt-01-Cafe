package com.miniproject.cafe.Controller;

import com.miniproject.cafe.Service.OrderService;
import com.miniproject.cafe.VO.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/admin/order-list")
    public String getAdminOrderListPage(Model model) {

        // 1. OrderService를 호출하여 모든 주문 목록을 가져옵니다.
        // (수정) OrderVO 리스트로 받습니다.
        List<OrderVO> orderList = orderService.getAdminOrderList();

        // 2. Model 객체에 주문 목록을 "orders"라는 이름으로 추가합니다.
        //    (이제 HTML 파일에서 이 데이터를 사용할 수 있습니다.)
        model.addAttribute("orders", orderList);

        // 3. resources/templates/admin/admin_order_list_3x2.html 파일을 반환합니다.
        return "admin/admin_order_list";
    }
}

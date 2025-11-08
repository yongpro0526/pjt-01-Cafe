package com.miniproject.cafe.Controller;

import com.miniproject.cafe.Service.OrderService;
import com.miniproject.cafe.VO.OrderVO;
// import com.miniproject.cafe.Service.OrderService; // (중요) 실제 OrderService를 임포트해야 합니다.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//주문 REST API 컨트롤러
@CrossOrigin // (CORS 오류 방지)
@RestController
@RequestMapping("/api/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderVO> createOrder(@RequestBody OrderVO order) {
        try {
            OrderVO createdOrder = orderService.createOrder(order);
            return ResponseEntity.ok(createdOrder);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/admin-list")
    public ResponseEntity<List<OrderVO>> getAdminOrderList() {
        List<OrderVO> orders = orderService.getAdminOrderList();
        return ResponseEntity.ok(orders);
    }

}
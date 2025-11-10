package com.miniproject.cafe.Controller;

import com.miniproject.cafe.VO.OrderVO;
import com.miniproject.cafe.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // (추가)

//주문 생성 및 관리 REST API
@CrossOrigin(origins = "http://localhost:8383")
@RestController
@RequestMapping("/api/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    //주문 생성
    @PostMapping("/create")
    public ResponseEntity<OrderVO> createOrder(@RequestBody OrderVO order) {
        try {
            OrderVO createdOrder = orderService.createOrder(order);
            return ResponseEntity.status(200).body(createdOrder); // 성공 응답 (HTTP 200 OK)
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    //주문 목록 조회
    @GetMapping("/admin-list")
    public ResponseEntity<List<OrderVO>> getAdminOrderList() {
        List<OrderVO> orders = orderService.getAdminOrderList();
        return ResponseEntity.ok(orders);
    }

    //주문 상태 업데이트 (완료, 취소)
    @PutMapping("/status/{orderId}")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody Map<String, String> payload) {

        String newStatus = payload.get("status"); // "주문완료" 또는 "주문취소"

        if (newStatus == null || newStatus.isEmpty()) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            // 서비스 호출
            orderService.updateOrderStatus(newStatus, orderId);
            return ResponseEntity.ok().build(); // 200 OK

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // 500 Internal Server Error
        }
    }
}
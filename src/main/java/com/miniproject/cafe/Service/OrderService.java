package com.miniproject.cafe.Service;

import com.miniproject.cafe.VO.OrderVO;

import java.util.List;

public interface OrderService {

    List<OrderVO> getAdminOrderList(); //관리자 페이지에 표시될 주문 목록
    OrderVO getOrderDetail(String orderId); //주문의 상세 정보
    OrderVO createOrder(OrderVO order); //주문 넣기
}

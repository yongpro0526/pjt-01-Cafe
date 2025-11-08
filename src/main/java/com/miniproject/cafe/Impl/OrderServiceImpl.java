package com.miniproject.cafe.Impl;

import com.miniproject.cafe.Mapper.OrderMapper;
import com.miniproject.cafe.Service.OrderService;
import com.miniproject.cafe.VO.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    /**
     * 필드 주입(Field Injection) 방식
     */
    @Autowired
    private OrderMapper orderMapper;

    //주문 들어온 조회
    @Override
    public List<OrderVO> getAdminOrderList() {
        return orderMapper.findAllAdminOrders();
    }

    //주문 상세
    @Override
    public OrderVO getOrderDetail(String orderId) {
        return orderMapper.findOrderById(orderId);
    }

    //주문 생성
    @Override
    public OrderVO createOrder(OrderVO order) {
        orderMapper.insertOrder(order);
        return order;
    }
}

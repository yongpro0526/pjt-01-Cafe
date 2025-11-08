package com.miniproject.cafe.Mapper;

import com.miniproject.cafe.VO.OrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderVO> findAllAdminOrders();
    OrderVO findOrderById(String orderId);
    void insertOrder(OrderVO order); //주문조회
}

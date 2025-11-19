package com.miniproject.cafe.Mapper;

import com.miniproject.cafe.VO.OrderItemVO;
import com.miniproject.cafe.VO.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderVO> findOrdersByStore(@Param("storeName") String storeName);
    void insertOrder(OrderVO order); //주문조회
    void insertOrderDetails(List<OrderItemVO> list);
    void updateOrderStatus(@Param("status") String status,
                           @Param("orderId") Long orderId); //주문 상태 완료 취소


    String findStoreNameByOrderId(@Param("orderId") Long orderId);

    //매장 필터가 포함된 단일 주문 조회
    OrderVO findOrderById(@Param("orderId") Long orderId,
                          @Param("storeName") String storeName);
}

package com.miniproject.cafe.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderVO {

    private Long orderId;      // 주문 번호 (#0001)
    private Date orderTime;     // 주문 시간
    private int totalQuantity;    // 총 수량
    private int totalPrice;       // 총 금액
    private String orderType; // 주문 유형 (매장, 포장, 배달)
    private String storeName; //매장명

    private String uId;
    private String orderStatus;

    private List<OrderItemVO> orderItemList;

    private Long dailyOrderNum;
}
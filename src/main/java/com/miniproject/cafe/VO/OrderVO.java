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

    private String orderId;       // 주문 번호 (예: "#103")
    private Date orderTime;     // 주문 시간 (날짜 포함)
    private int totalQuantity;    // 총 수량 (예: 3)
    private int totalPrice;       // 총 금액 (예: 9000)
    private String orderType;

    private String uId;
    private String orderStatus;// 주문 유형 (예: "매장", "포장", "배달")

    private List<OrderItemVO> orderItemList;
}
package com.miniproject.cafe.Service;

import com.miniproject.cafe.VO.CartItemVO;
import com.miniproject.cafe.VO.CartVO;

import java.util.Map;

public interface CartService {

    int insertCart(CartVO cartVO); // 장바구니 생성
    Map<String, Object> getCartList(String memberId); // 장바구니 목록 조회
    int addCartItem(CartItemVO cartItemVO); // 장바구니에 담기

}

package com.miniproject.cafe.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuVO {

    private String menuId;      // 메뉴 ID (PK)
    private String menuName;    // 메뉴 이름 (예: "아메리카노")
    private int price;          // 메뉴 가격 (예: 4500)
    private String description; // 메뉴 설명
    private String imageUrl;    // 메뉴 이미지 URL
    private String category;    // 메뉴 카테고리 (예: "커피", "음료", "디저트")
}
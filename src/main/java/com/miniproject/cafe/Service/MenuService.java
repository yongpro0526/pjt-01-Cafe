package com.miniproject.cafe.Service;

import com.miniproject.cafe.VO.MenuVO;

import java.util.List;

public interface MenuService {

    List<MenuVO> getAllMenuList(); //주문 가능한 모든 메뉴 목록
    List<MenuVO> getMenuListByCategory(String category); //특정 카테고리의 메뉴 목록 조회
    MenuVO getMenuById(String menuId); //메뉴 ID로 메뉴의 상세 정보 조회
}

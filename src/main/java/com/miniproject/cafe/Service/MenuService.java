package com.miniproject.cafe.Service;

import com.miniproject.cafe.VO.MenuVO;


import java.util.List;

public interface MenuService {
    List<MenuVO> getAllMenu();
    List<MenuVO> getMenuByCategory(String category);
    MenuVO getMenuById(String menuId);
    int insertMenu(MenuVO menu); //메뉴추가
    int updateMenu(MenuVO menu); //메뉴수정
    int deleteMenu(String menuId); //메뉴삭제
}

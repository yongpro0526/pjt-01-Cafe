package com.miniproject.cafe.Mapper;

import com.miniproject.cafe.VO.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<MenuVO> getAllMenu();
    List<MenuVO> getMenuByCategory(String category);
    MenuVO getMenuById(String menuId);
    int insertMenu(MenuVO menu); //메뉴추가
    int updateMenu(MenuVO menu); //메뉴수정
    int deleteMenu(String menuId); //메뉴삭제
}

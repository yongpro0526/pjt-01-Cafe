package com.miniproject.cafe.Mapper;

import com.miniproject.cafe.VO.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    List<MenuVO> findAllMenuList(); //모든 메뉴 조회
    List<MenuVO> findMenuListByCategory(String category); //카테고리별 메뉴 조회
    MenuVO findMenuById(String menuId); //메뉴id로 1건 조회
}
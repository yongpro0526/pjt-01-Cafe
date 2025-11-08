package com.miniproject.cafe.Controller;

import com.miniproject.cafe.Service.MenuService;
import com.miniproject.cafe.VO.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 모든 메뉴 조회 API
     */
    @GetMapping("/all")
    public ResponseEntity<List<MenuVO>> getAllMenuList() {
        List<MenuVO> menus = menuService.getAllMenuList();
        return ResponseEntity.ok(menus);
    }

    /**
     * 카테고리별 메뉴 조회 API
     * @param category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuVO>> getMenuListByCategory(@PathVariable("category") String category) {
        List<MenuVO> menus = menuService.getMenuListByCategory(category);
        return ResponseEntity.ok(menus);
    }

    /**
     * 특정 메뉴 상세 조회 API
     * @param menuId
     */
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuVO> getMenuById(@PathVariable("menuId") String menuId) {
        MenuVO menu = menuService.getMenuById(menuId);
        if (menu != null) {
            return ResponseEntity.ok(menu);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

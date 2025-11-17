package com.miniproject.cafe.VO;

import lombok.Data;

@Data
public class MenuVO {
    private String menuId;
    private String menuName;
    private String category;
    private String salesStatus;
    private String menuImg;
    private String menuPrice;
    private int hotAvailable ;
    private String menuDefinition;
}

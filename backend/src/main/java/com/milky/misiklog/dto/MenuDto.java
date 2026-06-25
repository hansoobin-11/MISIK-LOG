package com.milky.misiklog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MenuDto {
    private Long menuId;
    private Long restaurantId;
    private String menuName;
    private Integer menuPrice;
    private String menuInfo;
}
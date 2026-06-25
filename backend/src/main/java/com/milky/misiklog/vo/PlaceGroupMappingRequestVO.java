package com.milky.misiklog.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PlaceGroupMappingRequestVO {

    private List<Long> restaurantIds; 
    private Long placeGroupId;        
}

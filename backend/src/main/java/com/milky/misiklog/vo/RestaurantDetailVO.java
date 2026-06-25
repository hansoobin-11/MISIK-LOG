package com.milky.misiklog.vo;


import com.milky.misiklog.dto.RestaurantDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RestaurantDetailVO {
	private RestaurantDto restaurantDto;
	private String holidayDates;
	private String placeDepth1;
	private String placeDepth2;
	private String placeDepth3;
	private Long categoryNo;
	private String categoryName;
	private double restaurantAvgRating;
	private Integer reviewCount;
	private Integer restaurantMaxPeople;
	private Long placeGroupId;
	private String placeGroupName;
	
}

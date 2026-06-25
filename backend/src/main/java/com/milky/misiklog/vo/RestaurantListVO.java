package com.milky.misiklog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class RestaurantListVO {

	private Long restaurantId;
	private String restaurantName;
	private String restaurantAddress;
	private String restaurantOpen;
	private String restaurantClose;
	private String restaurantLastOrder;
	private String restaurantOpeningDays;
	private String holidayDates;
	private double restaurantAvgRating;
	private int reviewCount;
	private Long placeId;
	private String placeDepth1;
	private String placeDepth2;
	private String placeDepth3;
	private Long placeGroupId;
	private String placeGroupName;
	private Long categoryNo;
	private String categoryName;
	private Integer restaurantMaxPeople;
}

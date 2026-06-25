package com.milky.misiklog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//평균 리뷰 조회
@Data @AllArgsConstructor @Builder @NoArgsConstructor
public class ReviewListVO {
	public Long restaurantId;
	public double restaurantAvgRating;
	public int reviewCount;
}


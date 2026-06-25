package com.milky.misiklog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SearchVO {

	private String keyword;     // 서울, 강남, 한식, 소고기(통합검색 구현)
	private Long placeGroupId;     // 지역 필터  (핫플, 서울권 등)
    private Long categoryId;       // 카테고리 필터 (상/하위 카테고리)
}

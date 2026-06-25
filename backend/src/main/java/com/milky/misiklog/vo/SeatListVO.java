package com.milky.misiklog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SeatListVO {
	private String seatType;
	private String seatMaxPeople;
	private Integer count;
}

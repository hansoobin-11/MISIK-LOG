package com.milky.misiklog.vo;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SlotListVO {
	private Long restaurantId;
	private String restaurantName;
	private String reservationTime;
	private Integer totalSeatCount;
	private Integer reservedSeatCount;
}

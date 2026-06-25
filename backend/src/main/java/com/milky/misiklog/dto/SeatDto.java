package com.milky.misiklog.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SeatDto {
	private Long seatId;
	private Long seatRestaurantId;
	private String seatType;
	private Integer seatMaxPeople;
}

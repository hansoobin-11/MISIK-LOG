package com.milky.misiklog.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RestaurantHolidayDto {
	private Long restaurantId;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate restaurantHolidayDate;
}

package com.milky.misiklog.vo;

import com.milky.misiklog.dto.SeatDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SeatVO {
	@NotNull
	private SeatDto seatDto;
	
	@NotNull
	@Min(1)
	@Max(20)
	private Integer count;
}

package com.milky.misiklog.vo;

import com.milky.misiklog.dto.RestaurantDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//중복 검사 및 결제 진행을 위한 임시 데이터
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReservationReadyVO {
	private RestaurantDto restaurantDto;
	private Long reservationId;
}

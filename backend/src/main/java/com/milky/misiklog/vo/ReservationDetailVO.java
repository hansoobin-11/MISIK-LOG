package com.milky.misiklog.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder @Data @NoArgsConstructor @AllArgsConstructor
public class ReservationDetailVO {
	private Long reservationId;
	private String reservationMember;
	private Long reservationTarget;
	private Long reservationSeat;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime reservationTime;
	private Integer reservationPeopleCount;
	private String reservationStatus;
	private String reservationRequestNote;
	private String reservationPurpose;
	
	private String restaurantName;
	private String restaurantAddress;
	private String restaurantContact;
	private int paymentTotalAmount;
	private int refundAmount;
	
	private String tid;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	private LocalDateTime paymentTime;
}

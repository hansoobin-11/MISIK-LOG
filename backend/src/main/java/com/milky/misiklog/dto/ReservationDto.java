package com.milky.misiklog.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ReservationDto {
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
}

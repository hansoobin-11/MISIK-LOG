package com.milky.misiklog.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SlotLockDto {
	private long slotLockId;
	private long seatId;
	private String slotLockedBy;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime slotLockTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime slotExpiredAt;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime slotLockedAt;
}

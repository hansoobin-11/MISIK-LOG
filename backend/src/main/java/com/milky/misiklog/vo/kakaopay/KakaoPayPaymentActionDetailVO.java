package com.milky.misiklog.vo.kakaopay;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class KakaoPayPaymentActionDetailVO {
	private String aid;
	private LocalDateTime approvedAt;
	private Integer amount;
	private Integer pointAmount;
	private Integer discountAmount;
	private Integer greenDeposit;
	private String paymentActionType;
	private String payload;

}
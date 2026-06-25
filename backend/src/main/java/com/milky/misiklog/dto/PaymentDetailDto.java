package com.milky.misiklog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class PaymentDetailDto {
	private Long paymentDetailNo;
	private Long paymentDetailOrigin;
	private Long paymentDetailItemNo;
	private String paymentDetailItemName;
	private Integer paymentDetailItemPrice;
	private Integer paymentDetailQty;
	private String paymentDetailStatus;

	public Integer getPaymentDetailTotal() {//구매합계
		return paymentDetailItemPrice * paymentDetailQty;
	}

}

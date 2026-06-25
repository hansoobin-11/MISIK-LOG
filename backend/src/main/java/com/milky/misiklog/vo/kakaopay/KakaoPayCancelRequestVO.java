package com.milky.misiklog.vo.kakaopay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class KakaoPayCancelRequestVO {
	private String tid;//결제 고유 번호
	private int cancelAmount;//취소 금액
}

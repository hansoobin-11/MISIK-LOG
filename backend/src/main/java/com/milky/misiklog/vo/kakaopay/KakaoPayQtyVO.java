package com.milky.misiklog.vo.kakaopay;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class KakaoPayQtyVO {
	private long no;
	private int qty;
}

package com.milky.misiklog.vo.kakaopay;

import java.util.List;

import com.milky.misiklog.dto.ReservationDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//카카오페이의 특성상 서버 처리 시 유지할 데이터를 저장하는 객체
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class KakaoPayFlashVO {
	private String partnerOrderId;// 주문번호
	private String partnerUserId;// 주문자
	private String tid;// 거래번호
	private String returnUrl;// 복귀 주소
	private List<KakaoPayQtyVO> qtyList;
	private Long reservationId;
	private ReservationDto reservationDto;

}

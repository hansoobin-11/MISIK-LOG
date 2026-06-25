package com.milky.misiklog.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.milky.misiklog.configuration.KakaoPayProperties;
import com.milky.misiklog.vo.kakaopay.KakaoPayApproveRequestVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayApproveResponseVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayCancelRequestVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayCancelResponseVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayOrderRequestVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayOrderResponseVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayReadyRequestVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayReadyResponseVO;

@Service
public class KakaoPayService {
	@Autowired
	private WebClient webClient;
	@Autowired
	private KakaoPayProperties kakaoPayProperties;
	
	//결제 준비
	public KakaoPayReadyResponseVO ready(KakaoPayReadyRequestVO requestVO) {
		Map<String, String> body = new HashMap<>();
		body.put("cid", kakaoPayProperties.getCid());
		body.put("partner_order_id", requestVO.getPartnerOrderId());
		body.put("partner_user_id", requestVO.getPartnerUserId());
		body.put("item_name", requestVO.getItemName());
		body.put("quantity", "1");//수량(무조건 1, 우리가 관리)
		body.put("total_amount", String.valueOf(requestVO.getTotalAmount()));//판매금액
		body.put("tax_free_amount", "0");//비과세액(해당없음, 0으로 설정)
		
		String currentPath = ServletUriComponentsBuilder.fromCurrentRequest().toUriString();
		
		body.put("approval_url", currentPath+"/success/" + requestVO.getPartnerOrderId());
		body.put("cancel_url", currentPath+"/cancel/" + requestVO.getPartnerOrderId());
		body.put("fail_url", currentPath+"/fail/" + requestVO.getPartnerOrderId());

		KakaoPayReadyResponseVO responseVO = webClient.post()
				.uri("/online/v1/payment/ready")
				.bodyValue(body)
				.retrieve()
				.bodyToMono(KakaoPayReadyResponseVO.class)
				.block();
		
		return responseVO;
		
	}
	//결제 승인
	public KakaoPayApproveResponseVO approve(KakaoPayApproveRequestVO requestVO) {
		Map<String, String> body = new HashMap<>();
		//body에 필요한 정보들을 담음 (카카오페이 문서 확인)
		body.put("cid", kakaoPayProperties.getCid());
		body.put("partner_order_id", requestVO.getPartnerOrderId());
		body.put("partner_user_id", requestVO.getPartnerUserId());
		body.put("tid", requestVO.getTid());
		body.put("pg_token", requestVO.getPgToken());
		
		KakaoPayApproveResponseVO response = webClient.post()//POST 요청
				.uri("/online/v1/payment/approve")//webClient에 기본주소 설정이 있을 경우
				.bodyValue(body)//요청에 첨부할 데이터 설정
			.retrieve()//응답을 수신하겠다
				.bodyToMono(KakaoPayApproveResponseVO.class)//데이터는 한번에 오고(Mono) 형태는 KakaoPayApproveResponseVO이다 (↔ 연속적으로 오면 Flux)
				.block();//동기적으로 변환하여 응답이 올때까지 기다려라! (RestTemplate과 같아짐)
		
		return response;
	}
	//결제조회
	public KakaoPayOrderResponseVO order(KakaoPayOrderRequestVO requestVO) {
		Map<String, String> body = new HashMap<>();
		body.put("cid", kakaoPayProperties.getCid());
		body.put("tid", requestVO.getTid());
		
		KakaoPayOrderResponseVO responseVO = webClient.post()//POST 요청
				.uri("/online/v1/payment/order")//webClient에 기본주소 설정이 있을 경우
				.bodyValue(body)//요청에 첨부할 데이터 설정
			.retrieve()//응답을 수신하겠다
				.bodyToMono(KakaoPayOrderResponseVO.class)//데이터는 한번에 오고(Mono) 형태는 KakaoPayApproveResponseVO이다 (↔ 연속적으로 오면 Flux)
				.block();//동기적으로 변환하여 응답이 올때까지 기다려라! (RestTemplate과 같아짐)
		
		return responseVO;
	}
	//결제취소
	public KakaoPayCancelResponseVO cancel(KakaoPayCancelRequestVO requestVO) {
		Map<String, String> body = new HashMap<>();
		body.put("cid", kakaoPayProperties.getCid());
		body.put("tid", requestVO.getTid());
		body.put("cancel_amount", String.valueOf(requestVO.getCancelAmount()));
		body.put("cancel_tax_free_amount", "0");
		
		KakaoPayCancelResponseVO responseVO = webClient.post()//POST 요청
				.uri("/online/v1/payment/cancel")//webClient에 기본주소 설정이 있을 경우
				.bodyValue(body)//요청에 첨부할 데이터 설정
			.retrieve()//응답을 수신하겠다
				.bodyToMono(KakaoPayCancelResponseVO.class)//데이터는 한번에 오고(Mono) 형태는 KakaoPayApproveResponseVO이다 (↔ 연속적으로 오면 Flux)
				.block();//동기적으로 변환하여 응답이 올때까지 기다려라! (RestTemplate과 같아짐)
		
		return responseVO;
	}
}

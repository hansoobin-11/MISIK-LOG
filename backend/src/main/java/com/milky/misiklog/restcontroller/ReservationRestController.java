package com.milky.misiklog.restcontroller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.milky.misiklog.dao.ReservationDao;
import com.milky.misiklog.dao.SlotLockDao;
import com.milky.misiklog.dto.ReservationDto;
import com.milky.misiklog.dto.RestaurantDto;
import com.milky.misiklog.error.ReservationConflictException;
import com.milky.misiklog.service.KakaoPayService;
import com.milky.misiklog.service.PaymentService;
import com.milky.misiklog.service.ReservationService;
import com.milky.misiklog.vo.ReservationDetailVO;
import com.milky.misiklog.vo.ReservationReadyVO;
import com.milky.misiklog.vo.TokenVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayApproveRequestVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayApproveResponseVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayFlashVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayReadyRequestVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayReadyResponseVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "예약 관리 컨트롤러")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/reservation")
public class ReservationRestController {
	@Autowired
	private KakaoPayService kakaoPayService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private ReservationDao reservationDao;
	
	private Map<String, KakaoPayFlashVO> flashMap = Collections.synchronizedMap(new HashMap<>());
	
	@PostMapping("/pay")
	public KakaoPayReadyResponseVO buy (
			@RequestBody ReservationDto reservationDto, 
			@RequestHeader("Frontend-Url") String frontendUrl, 
			@RequestAttribute TokenVO tokenVO
			) {
		
		//중복 검사
		reservationDto.setReservationMember(tokenVO.getLoginId());
		
		ReservationReadyVO readyVO = reservationService.checkConflictAndSendTarget(reservationDto);
		
		RestaurantDto target = readyVO.getRestaurantDto();
		
		Long reservationId = readyVO.getReservationId();
		
		int price = target.getRestaurantReservationPrice();
		int people = reservationDto.getReservationPeopleCount();
		
		int total = price * people ; //예약금 * 인원
		
		String targetName = target.getRestaurantName();
		
		String itemName = "No." + reservationId + " ( " + targetName + " ) 예약금 결제 건";
		
		KakaoPayReadyRequestVO requestVO = KakaoPayReadyRequestVO.builder()
				.partnerOrderId(UUID.randomUUID().toString())
				.partnerUserId(tokenVO.getLoginId())
				.itemName(itemName)
				.totalAmount(total)
				.build();
		
			
		KakaoPayReadyResponseVO responseVO = kakaoPayService.ready(requestVO);
		
		reservationDto.setReservationId(reservationId);
		
		reservationService.createReservation(reservationDto);
		
		flashMap.put(requestVO.getPartnerOrderId(), 
				KakaoPayFlashVO.builder()
				.partnerOrderId(requestVO.getPartnerOrderId())
				.partnerUserId(requestVO.getPartnerUserId())
				.tid(responseVO.getTid())
				.returnUrl(frontendUrl)
				.reservationId(reservationId)
				.reservationDto(reservationDto)
				.build());
		
		return responseVO;
	}
	
	
	@GetMapping("/pay/success/{partnerOrderId}")
	public void success(@PathVariable String partnerOrderId,
			@RequestParam("pg_token") String pgToken, HttpServletResponse response) throws IOException {
		
		KakaoPayFlashVO flashVO = flashMap.remove(partnerOrderId);
		
		if(flashVO == null) return;
		
		//결제 승인을 위한 데이터 생성
				KakaoPayApproveRequestVO requestVO = KakaoPayApproveRequestVO.builder()
							.partnerOrderId(flashVO.getPartnerOrderId())
							.partnerUserId(flashVO.getPartnerUserId())
							.tid(flashVO.getTid())
							.pgToken(pgToken)
						.build();
				
				//결제 승인 요청
				KakaoPayApproveResponseVO responseVO = kakaoPayService.approve(requestVO);
				
				//DB 저장 코드 (payment, payment_detail)
				paymentService.insert(responseVO, flashVO);
				
				
				response.sendRedirect(flashVO.getReturnUrl() + "/success?reservationId=" + flashVO.getReservationId());
	}
	
	@GetMapping("/pay/cancel/{partnerOrderId}")
	public void cancel(@PathVariable String partnerOrderId, HttpServletResponse response) throws IOException {
		KakaoPayFlashVO flashVO = flashMap.remove(partnerOrderId);
		response.sendRedirect(flashVO.getReturnUrl() + "/cancel");
	}
	
	@GetMapping("/pay/fail/{partnerOrderId}")
	public void fail(@PathVariable String partnerOrderId, HttpServletResponse response) throws IOException {
		KakaoPayFlashVO flashVO = flashMap.remove(partnerOrderId);
		response.sendRedirect(flashVO.getReturnUrl() + "/fail");
	}
	
	@GetMapping("/detail/{reservationId}")
	public ReservationDetailVO detail(@PathVariable Long reservationId) {
		return reservationDao.findDetailVO(reservationId);
	}
	
	@GetMapping("/myList")
	public List<ReservationDetailVO> list(@RequestAttribute TokenVO tokenVO){
		String memberId = tokenVO.getLoginId();
		return reservationDao.findAllByMember(memberId);
	}
	
	//완료된 예약 취소(환불)
	@PostMapping("/refund")
	public void refundReservation(
			@RequestParam Long reservationId,
			@RequestAttribute TokenVO tokenVO) {
		reservationService.cancelReservation(reservationId);
		
		log.info("예약 환불 취소 완료: 번호={}, 사용자={}", reservationId, tokenVO.getLoginId());
	}
	
	//예약 상태 변경 (식당주인용 : 방문완료/노쇼)
	@PatchMapping("/status/{reservationId}")
	public void updateStatus(@PathVariable Long reservationId,
			@RequestBody ReservationDto reservationDto,
			@RequestAttribute TokenVO tokenVO) {
		
		reservationService.updateReservationStatusByOwner(reservationId, reservationDto.getReservationStatus(), tokenVO);
		
		log.info("예약 상태 변경 완료 : 번호={}, 변경 후 상태={}",
				reservationId, reservationDto.getReservationStatus());
	}
	
	@GetMapping("/ownerList")
	public List<ReservationDetailVO> ownerList(@RequestAttribute TokenVO tokenVO){
		String ownerId = tokenVO.getLoginId();
		return reservationService.getOwnerReservationList(ownerId);
	}

}

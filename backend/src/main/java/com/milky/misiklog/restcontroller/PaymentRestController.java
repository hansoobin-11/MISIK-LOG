package com.milky.misiklog.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milky.misiklog.dao.PaymentDao;
import com.milky.misiklog.dao.PaymentDetailDao;
import com.milky.misiklog.dto.PaymentDetailDto;
import com.milky.misiklog.dto.PaymentDto;
import com.milky.misiklog.error.NeedPermissionException;
import com.milky.misiklog.error.TargetNotFoundException;
import com.milky.misiklog.service.KakaoPayService;
import com.milky.misiklog.service.PaymentService;
import com.milky.misiklog.vo.TokenVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayCancelRequestVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayCancelResponseVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayOrderRequestVO;
import com.milky.misiklog.vo.kakaopay.KakaoPayOrderResponseVO;
import com.milky.misiklog.vo.kakaopay.PaymentInfoVO;

@CrossOrigin
@RestController
@RequestMapping("/payment")
public class PaymentRestController {
	@Autowired
	private PaymentDao paymentDao;
	@Autowired
	private PaymentDetailDao paymentDetailDao;
	@Autowired
	private KakaoPayService kakaoPayService;
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/account")
	public List<PaymentDto> listByOwner(@RequestAttribute TokenVO tokenVO){
		return paymentDao.selectList(tokenVO);
	}
	@GetMapping("/{paymentNo}")
	public PaymentInfoVO detail(@PathVariable long paymentNo,
			@RequestAttribute TokenVO tokenVO) {
		PaymentDto paymentDto = paymentDao.selectOne(paymentNo);
		if(paymentDto == null) throw new TargetNotFoundException();
		
		boolean isOwner = paymentDto.getPaymentOwner().equals(tokenVO.getLoginId());
		if(isOwner==false) throw new NeedPermissionException();
		
		List<PaymentDetailDto> paymentDetailList = paymentDetailDao.selectList(paymentNo);
		
		KakaoPayOrderResponseVO responseVO = kakaoPayService.order(KakaoPayOrderRequestVO.builder()
				.tid(paymentDto.getPaymentTid())
				.build());
		
		return PaymentInfoVO.builder()
				.paymentDto(paymentDto)
				.paymentDetailList(paymentDetailList)
				.responseVO(responseVO)
				.build();
	}
	
	@DeleteMapping("/{paymentNo}")
	public void cancel(@PathVariable long paymentNo,
			@RequestAttribute TokenVO tokenVO) {
		PaymentDto paymentDto = paymentDao.selectOne(paymentNo);
		if(paymentDto == null) throw new TargetNotFoundException();
		boolean isOwner = paymentDto.getPaymentOwner().equals(tokenVO.getLoginId());
		if(isOwner == false) throw new NeedPermissionException();
		
		KakaoPayCancelRequestVO requestVO = KakaoPayCancelRequestVO.builder()
				.tid(paymentDto.getPaymentTid())
				.cancelAmount(paymentDto.getPaymentRemain())
				.build();
		
		KakaoPayCancelResponseVO responseVO = kakaoPayService.cancel(requestVO);
		
		if(responseVO !=null) {
			paymentService.cancel(paymentNo);
		}else {
			throw new TargetNotFoundException();
		}
	}
}

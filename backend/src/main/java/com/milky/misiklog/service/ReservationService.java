package com.milky.misiklog.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.milky.misiklog.dao.PaymentDao;
import com.milky.misiklog.dao.ReservationDao;
import com.milky.misiklog.dao.RestaurantDao;
import com.milky.misiklog.dao.SlotLockDao;
import com.milky.misiklog.dto.PaymentDto;
import com.milky.misiklog.dto.ReservationDto;
import com.milky.misiklog.dto.RestaurantDto;
import com.milky.misiklog.error.NeedPermissionException;
import com.milky.misiklog.error.ReservationConflictException;
import com.milky.misiklog.vo.ReservationDetailVO;
import com.milky.misiklog.vo.ReservationReadyVO;
import com.milky.misiklog.vo.TokenVO;

import lombok.extern.slf4j.Slf4j;

//결제 및 예약 동시 처리를 위해 통합
@Slf4j
@Service
public class ReservationService {
	@Autowired
	private ReservationDao reservationDao;

	@Autowired
	private RestaurantDao restaurantDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private SlotLockDao slotLockDao;
	
	// 결제 시작 전 중복 체크 & 식당 정보 제공
	public ReservationReadyVO checkConflictAndSendTarget(ReservationDto reservationDto) {

		boolean isAvailable = reservationDao.checkAvailableSlot(reservationDto);

		// 중복 검사 결과가 false인 경우 예외 처리(예약 충돌)
		if (!isAvailable)
			throw new ReservationConflictException("이미 예약된 좌석입니다");

		Long restaurantId = reservationDto.getReservationTarget();

		RestaurantDto target = restaurantDao.selectOne(restaurantId);
		
		long seatId = reservationDto.getReservationSeat();
		
		LocalDateTime reservationTime = reservationDto.getReservationTime();
		
		boolean isLockUser = slotLockDao.lockedUser(seatId, reservationTime) == reservationDto.getReservationMember();
		
		if(isLockUser == false) throw new ReservationConflictException("이미 선점된 좌석입니다");
		
		ReservationReadyVO readyVO = ReservationReadyVO.builder().restaurantDto(target)
				.reservationId(reservationDao.sequence()).build();
		return readyVO;
	}

	// DB에 예약 정보 등록
	@Transactional
	public void createReservation(ReservationDto reservationDto) {

		// DB 저장 전에도 중복 예약 여부 확인
		boolean isAvailable = reservationDao.checkAvailableSlot(reservationDto);

		// 중복 검사 결과가 false인 경우 예외 처리(예약 충돌)
		if (!isAvailable)
			throw new ReservationConflictException("이미 예약된 좌석입니다");

		reservationDao.insert(reservationDto);
		
		long seatId = reservationDto.getReservationSeat();
		LocalDateTime reservationTime = reservationDto.getReservationTime();
		
		long slotLockId = slotLockDao.selectOneByReservation(seatId, reservationTime);
		
		//lock 제거
		slotLockDao.deleteLock(slotLockId);
	}

	@Transactional
	public void cancelReservation(Long reservationId) {
		// 예약 정보 조회
		ReservationDto reservationDto = reservationDao.selectOne(reservationId);

		// 환불 퍼센트 계산
		LocalDate today = LocalDate.now();
		LocalDate visitDate = reservationDto.getReservationTime().toLocalDate();
		int percent;

		if (today.isBefore(visitDate.minusDays(1))) {
			percent = 100;
		} else if (today.isEqual(visitDate.minusDays(1))) {
			percent = 50;
		} else {
			percent = 0;
		}
		
		//해당 예약의 결제 정보 가져오기
		PaymentDto paymentDto = paymentDao.selectOneByReservationId(reservationId);
		
		//환불 금액 계산
		int refundAmount = (int) (paymentDto.getPaymentTotal() * (percent/100.0));
		
		//PaymentService를 통해 실제 결제 취소 진행
		paymentService.cancel(paymentDto.getPaymentNo(), refundAmount);
		
		paymentDao.cancel(paymentDto.getPaymentNo(), refundAmount);
		
		//예약 상태 변경
		reservationDao.updateStatus(reservationId, "예약취소");

	}
	
	@Transactional
	public void updateReservationStatusByOwner(Long reservationId, String newStatus, TokenVO tokenVO) {
		String ownerId = reservationDao.getOwnverIdByReservationId(reservationId);
		
		boolean isOwner = tokenVO.getLoginId().equals(ownerId);
		boolean isAdmin = tokenVO.getLoginLevel().equals("관리자");
		
		if(!isOwner && !isAdmin) {
			throw new NeedPermissionException("해당 예약 상태를 변경할 수 있는 권한이 없습니다.");
		}
		boolean result = reservationDao.updateStatus(reservationId, newStatus);
		
		if(!result) {
			throw new RuntimeException("예약 상태 업데이트에 실패했습니다.");
		}
		
		if(newStatus.equals("방문완료")) {
			log.info("방문완료 처리됨 - 예약번호: {}", reservationId);
		}
	}
	
	public List<ReservationDetailVO> getOwnerReservationList(String ownerId){
		return reservationDao.findAllByOwner(ownerId);
	}
	
	
}

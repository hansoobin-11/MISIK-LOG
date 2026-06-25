package com.milky.misiklog.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.PaymentDto;
import com.milky.misiklog.vo.TokenVO;

@Repository
public class PaymentDao {
	@Autowired
	private SqlSession sqlSession;
	
	public long sequence() {
		return sqlSession.selectOne("payment.sequence");
	}
	
	public void insert(PaymentDto paymentDto) {
		sqlSession.insert("payment.insert",paymentDto);
	}
	
	public List<PaymentDto> selectList(TokenVO tokenVO){
		return sqlSession.selectList("payment.listByOwner",tokenVO);
	}
	
	public PaymentDto selectOne(long paymentNo) {
		return sqlSession.selectOne("payment.detail",paymentNo);
	}
	
	public boolean cancel(long paymentNo) {
		return sqlSession.update("payment.cancel", paymentNo)>0;
		
	}
	public boolean cancel(long paymentNo, int refundAmount) {
		PaymentDto paymentDto = PaymentDto.builder()
				.paymentNo(paymentNo)
				.paymentRefund(refundAmount)
				.build();
		return sqlSession.update("payment.cancel", paymentDto)>0;
		
	}

	public PaymentDto selectOneByReservationId(Long reservationId) {
		return sqlSession.selectOne("payment.selectOneByReservationId", reservationId);
	}
	
}

package com.milky.misiklog.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.PaymentDetailDto;


@Repository
public class PaymentDetailDao {

	@Autowired
	private SqlSession sqlSession;

	public long sequence() {
		return sqlSession.selectOne("paymentDetail.sequence");
	}

	public void insert(PaymentDetailDto paymentDetailDto) {
		sqlSession.insert("paymentDetail.insert", paymentDetailDto);
	}

	public List<PaymentDetailDto> selectList(long paymentDetailOrigin) {
		return sqlSession.selectList("paymentDetail.listByOrigin", paymentDetailOrigin);
	}

	public boolean cancel(long paymentDetailOrigin) {
		return sqlSession.update("paymentDetail.cancel", paymentDetailOrigin) > 0;
	}

	public PaymentDetailDto selectOne(long paymentDetailNo) {
		return sqlSession.selectOne("paymentDetail.detail",paymentDetailNo);
	}
}

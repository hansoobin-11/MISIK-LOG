package com.milky.misiklog.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.ReservationDto;
import com.milky.misiklog.error.TargetNotFoundException;
import com.milky.misiklog.vo.ReservationDetailVO;

@Repository
public class ReservationDao {
	@Autowired
	private SqlSession sqlSession;

	public long sequence() {
		return sqlSession.selectOne("reservation.sequence");
	}

	public boolean checkAvailableSlot(ReservationDto reservationDto) {
		int count = sqlSession.selectOne("reservation.checkSlot", reservationDto);
		return count == 0; // 예약 가능
	}

	public ReservationDto insert(ReservationDto reservationDto) {

		sqlSession.insert("reservation.insert", reservationDto);

		return reservationDto;
	}

	public ReservationDto selectOne(long reservationId) {
		ReservationDto reservationDto = sqlSession.selectOne("reservation.detail", reservationId);
		if (reservationDto == null)
			throw new TargetNotFoundException();
		return reservationDto;
	}

	public ReservationDetailVO findDetailVO(Long reservationId) {
		return sqlSession.selectOne("reservation.detailVO", reservationId);
	}

	public List<ReservationDetailVO> findAllByMember(String memberId) {
		return sqlSession.selectList("reservation.listByMember", memberId);
	}

	public boolean updateStatus(Long reservationId, String status) {
		Map<String, Object> params = new HashMap<>();
		params.put("reservationId", reservationId);
		params.put("status", status);
		return sqlSession.update("reservation.updateStatus", params) > 0;
	}

	//자영업자 식당의 예약 목록 전체 조회
	public List<ReservationDetailVO> findAllByOwner(String ownerId) {
		return sqlSession.selectList("reservation.listByOwner", ownerId);
	}

	//예약 번호로 사장id 찾기
	public String getOwnverIdByReservationId(Long reservationId) {
		return sqlSession.selectOne("reservation.getOwnerId", reservationId);
	}
}

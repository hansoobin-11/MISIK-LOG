package com.milky.misiklog.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.SeatDto;
import com.milky.misiklog.error.TargetNotFoundException;
import com.milky.misiklog.vo.SlotListVO;

@Repository
public class SeatDao {
	@Autowired
	private SqlSession sqlSession;
	
	public SeatDto insert(SeatDto seatDto) {
		long sequence = sqlSession.selectOne("seat.sequence");
		seatDto.setSeatId(sequence);
		sqlSession.insert("seat.insert", seatDto);
		return seatDto;
	}
	
	public SeatDto selectOne(long seatId) {
		SeatDto seatDto = sqlSession.selectOne("seat.detail", seatId);
		if(seatDto == null) throw new TargetNotFoundException();
		return seatDto;
	}
	
	//식당별 전체 좌석
	public List<SeatDto> selectList(long seatRestaurantId){
		return sqlSession.selectList("seat.list", seatRestaurantId);
	}
	
	//예약 가능한 좌석(목록 출력용)
	public List<SlotListVO> selectListWithReservation(long seatRestaurantId){
		return sqlSession.selectList("seat.listWithReservation", seatRestaurantId);
	}
	
	//예약 가능 좌석(상세)
	public List<SeatDto> selectAvailableSeatList(long restaurantId, String slotTime, int peopleCount){
		Map<String, Object> params = new HashMap<>();
		params.put("restaurantId", restaurantId);
		params.put("slotTime", slotTime);
		params.put("peopleCount", peopleCount);
		return sqlSession.selectList("seat.availableSeatType", params);
	}
	public void delete(long seatId) {
	    sqlSession.delete("seat.delete", seatId);
	}
}

package com.milky.misiklog.dao;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.SlotLockDto;
import com.milky.misiklog.error.TargetNotFoundException;

@Repository
public class SlotLockDao {
	@Autowired
	private SqlSession sqlSession;
	
	public SlotLockDto addLock(SlotLockDto slotLockDto) {
		long slotLockId = sqlSession.selectOne("slotLock.sequence");
		
		slotLockDto.setSlotLockId(slotLockId);
		
		sqlSession.insert("slotLock.addLock", slotLockDto);
		
		return sqlSession.selectOne("slotLock.detail", slotLockId);
	}
	
	public long selectOneByReservation(long seatId, LocalDateTime reservationTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("seatId", seatId);
		params.put("reservationTime", reservationTime);
		
		return sqlSession.selectOne("slotLock.idByReservation", params);
	}
	
	public String lockedUser(long seatId, LocalDateTime reservationTime) {
		Map<String, Object> params = new HashMap<>();
		params.put("seatId", seatId);
		params.put("reservationTime", reservationTime);
		
		return sqlSession.selectOne("slotLock.checkId", params);
	}
	public boolean deleteLock(long slotLockId) {
		SlotLockDto slotLockDto = sqlSession.selectOne("slotLock.detail", slotLockId);
		if(slotLockDto == null) throw new TargetNotFoundException();
		return sqlSession.delete("slotLock.delete", slotLockId) > 0;
	}
}

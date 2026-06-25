package com.milky.misiklog.restcontroller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milky.misiklog.dao.SeatDao;
import com.milky.misiklog.dao.SlotLockDao;
import com.milky.misiklog.dto.SeatDto;
import com.milky.misiklog.dto.SlotLockDto;
import com.milky.misiklog.vo.SlotListVO;
import com.milky.misiklog.vo.SlotRequestVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "좌석 관리 컨트롤러")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/slot")
public class SeatRestController {
	@Autowired
	private SeatDao seatDao;
	
	@Autowired
	private SlotLockDao slotLockDao;
	
	@GetMapping("/{restaurantId}")
	public List<SlotListVO> slotList(@PathVariable long restaurantId){
		return seatDao.selectListWithReservation(restaurantId);
	}
	
	//이용 가능 좌석 유형
	@PostMapping("/seat")
	public List<SeatDto> slotListDetail(@RequestBody SlotRequestVO slotRequestVO){
		
		long restaurantId= slotRequestVO.getRestaurantId();
		String slotTime = slotRequestVO.getSlotTime();
		int peopleCount = slotRequestVO.getPeopleCount();
		
		return seatDao.selectAvailableSeatList(restaurantId, slotTime, peopleCount);
	}
	
	@PostMapping("/lock")
	public long slotLock(@RequestBody SlotLockDto slotLockDto) {
		SlotLockDto result = slotLockDao.addLock(slotLockDto);
		long id = result.getSlotLockId();
		return id;
	}
	
	@DeleteMapping("/lock/delete/{slotLockId}")
	public void deleteLock(@PathVariable long slotLockId) {
		slotLockDao.deleteLock(slotLockId);
	}
	
	// 좌석 목록 (이 식당에 등록된 좌석 그대로)
	@GetMapping("/seat/list/{restaurantId}")
	public List<SeatDto> seatList(@PathVariable long restaurantId) {
	    return seatDao.selectList(restaurantId);
	}

	// 좌석 등록 (1개 단위)
	@PostMapping("/seat/add")
	public void addSeat(@RequestBody SeatDto seatDto) {
	    seatDao.insert(seatDto);
	}

	// 좌석 삭제 (1개 단위)
	@DeleteMapping("/seat/{seatId}")
	public void deleteSeat(@PathVariable long seatId) {
	    seatDao.delete(seatId);
	}
	
}

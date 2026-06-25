package com.milky.misiklog.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.milky.misiklog.dao.WishlistDao;
import com.milky.misiklog.dto.RestaurantDto;
import com.milky.misiklog.dto.WishlistDto;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "위시리스트 관리 컨트롤러")
@CrossOrigin
@RestController
@RequestMapping("/rest/wishlist")
public class WishlistRestController {

	@Autowired
	private WishlistDao wishlistDao;

	@PostMapping("/toggle")
	public Map<String, Object> toggle(@RequestBody WishlistDto wishlistDto) {
		boolean isCheck = wishlistDao.check(wishlistDto);

		if (isCheck) {
			wishlistDao.delete(wishlistDto);
		} else {
			wishlistDao.insert(wishlistDto);
		}

		int count = wishlistDao.countByRestaurantId(wishlistDto.getRestaurantId());

		Map<String, Object> result = new HashMap<>();
		result.put("isWish", !isCheck);
		result.put("count", count);

		return result;
	}

	@PostMapping("/check")
	public Map<String, Object> check(@RequestBody WishlistDto wishlistDto) {

		boolean isCheck = false;
		// memberId가 있을 때만 check DAO를 호출하도록 분기 처리
		if (wishlistDto.getMemberId() != null && !wishlistDto.getMemberId().isEmpty()) {
			isCheck = wishlistDao.check(wishlistDto);
		}

		// 개수는 로그인 여부와 상관없이 항상 조회
		int count = wishlistDao.countByRestaurantId(wishlistDto.getRestaurantId());

		Map<String, Object> result = new HashMap<>();
		result.put("isWish", isCheck); // 비회원은 항상 false
		result.put("count", count);

		return result;
	}

	@GetMapping("/myList")
	public List<RestaurantDto> selectList(@RequestParam String memberId) {
		if (memberId == null || memberId.trim().isEmpty()) {
			return List.of();
		}
		return wishlistDao.selectList(memberId);
	}

}

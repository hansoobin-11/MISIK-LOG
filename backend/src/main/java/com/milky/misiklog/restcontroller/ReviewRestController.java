package com.milky.misiklog.restcontroller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.milky.misiklog.dao.AttachmentDao;
import com.milky.misiklog.dao.ReviewDao;
import com.milky.misiklog.dto.ReviewDto;
import com.milky.misiklog.error.TargetNotFoundException;
import com.milky.misiklog.service.ReviewService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "리뷰 관리 컨트롤러")
@CrossOrigin
@RestController
@RequestMapping("/restaurant/detail/{restaurantId}/review")
public class ReviewRestController {
	@Autowired
	private ReviewDao reviewDao;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private AttachmentDao attachmentDao;

	// 리뷰 등록
	@PostMapping("/")
	public void insert(@PathVariable int restaurantId, 
	        @RequestParam(required = false) MultipartFile attach, // 1. 파일 바인딩 충돌 방지
	        @RequestParam String memberId, 
	        @RequestParam String reviewContent,
	        @RequestParam double reviewRating) throws IllegalStateException, IOException {
	    
	    // 2. DTO 수동 생성 및 설정
	    ReviewDto reviewDto = new ReviewDto();
	    reviewDto.setRestaurantId(restaurantId);
	    reviewDto.setMemberId(memberId);
	    reviewDto.setReviewContent(reviewContent);
	    reviewDto.setReviewRating(reviewRating);
//	    reviewDto.setReviewAttachmentNo(attachmentDao.sequence());
//	    System.out.println("레스트컨트롤러 실행");
	    reviewService.insert(reviewDto, attach);
//	    System.out.println("레스트컨트롤러 종료");
	}

	// 특정 식당의 리뷰 목록 조회
	@GetMapping("/")
	public List<ReviewDto> ListByRestaurant(@PathVariable int restaurantId) {
		return reviewDao.selectListByRestaurant(restaurantId);
	}
	
	//내가 쓴 리뷰
	@GetMapping("/myList")
	public List<ReviewDto> listByUser(@RequestParam String memberId) {
	    // ReviewDao에 구현된 특정 사용자의 리뷰 조회 메서드를 호출합니다.
		return reviewDao.selectListByUser(memberId);
	}

	// 리뷰 상세
	@GetMapping("/{reviewNo}")
	public ReviewDto detail(@PathVariable int restaurantId, @PathVariable int reviewNo) {
		return reviewDao.selectOne(reviewNo);
	}

	// 리뷰 수정
	@PutMapping("/{reviewNo}")
	public boolean update(@PathVariable int restaurantId, @PathVariable int reviewNo, ReviewDto reviewDto,
			@RequestParam(required = false) MultipartFile newAttach) {
		reviewDto.setReviewNo(reviewNo);
		reviewDto.setRestaurantId(restaurantId);

		// ⭐ Service 호출 (파일 수정 및 권한 확인)
		boolean result = reviewService.update(reviewDto, newAttach);

		if (!result) {
			throw new RuntimeException("리뷰 수정에 실패했거나 권한이 없습니다.");
		}
		return true;
	}

	// 리뷰 삭제
	@DeleteMapping("/{reviewNo}")
	public boolean delete(@PathVariable int restaurantId, @PathVariable int reviewNo) {
		boolean result = reviewService.delete(reviewNo);

		if (!result) {
			throw new TargetNotFoundException("삭제할 리뷰를 찾을 수 없거나 권한이 없습니다.");
		}
		return true;
	}
	
	

}

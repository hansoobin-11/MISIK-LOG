package com.milky.misiklog.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.milky.misiklog.dao.AttachmentDao;
import com.milky.misiklog.dao.RestaurantDao;
import com.milky.misiklog.dao.ReviewDao;
import com.milky.misiklog.dto.RestaurantDto;
import com.milky.misiklog.dto.ReviewDto;
import com.milky.misiklog.error.TargetNotFoundException;
import com.milky.misiklog.restcontroller.SeatRestController;
import com.milky.misiklog.vo.ReviewListVO;

@Service
public class ReviewService {

    private final SeatRestController seatRestController;

	@Autowired
	private ReviewDao reviewDao;

	@Autowired
	private AttachmentService attachmentService;

	@Autowired
	private AttachmentDao attachmentDao;
	
	@Autowired
	private RestaurantDao restaurantDao;
	
    ReviewService(SeatRestController seatRestController) {
        this.seatRestController = seatRestController;
    }
	
	@Transactional
	// ⭐ Checked Exception을 throws에 선언
	public void insert(ReviewDto reviewDto, MultipartFile attach) throws IllegalStateException, IOException {
//	    Integer attachmentNo = null;
//		reviewDto.setReviewAttachmentNo(attachmentDao.sequence());
		System.out.println("리뷰서비스 메소드 실행");
		System.out.println(reviewDto);
		System.out.println("attach=" + attach);
	    if (attach != null && !attach.isEmpty()) {
	        Integer attachmentNo = attachmentService.save(attach);
	        System.out.println("attachmentNo=" + attachmentNo);
	        reviewDto.setReviewAttachmentNo(attachmentNo);
	    }
	    reviewDao.insert(reviewDto);
	    
	    //리뷰 등록 시 식당 별점 평균 업데이트
	    ReviewListVO avg = reviewDao.reviewAvg(reviewDto.getRestaurantId());
	    
	    double avgRating = avg.getRestaurantAvgRating();
	    
	    restaurantDao.updateReviewAvg(reviewDto.getRestaurantId(), avgRating);
	    
	}

	@Transactional
	public boolean update(ReviewDto reviewDto, MultipartFile newAttach) {
		ReviewDto existingReview = reviewDao.selectOne(reviewDto.getReviewNo());
		if (existingReview == null) {
			throw new TargetNotFoundException("수정 대상 리뷰가 존재하지 않습니다.");
		}
		Integer oldAttachmentNo = existingReview.getReviewAttachmentNo();
		Integer finalAttachmentNo = oldAttachmentNo;

		try {
	        // 1. 새로운 파일이 들어온 경우 처리
	        if (newAttach != null && !newAttach.isEmpty()) {
	            finalAttachmentNo = attachmentService.save(newAttach);
	        } 
	        // 2. 새 파일은 없는데, 기존 파일을 삭제하기로 한 경우 (리액트에서 null로 보냈을 때)
	        else if (reviewDto.getReviewAttachmentNo() == null) {
	            finalAttachmentNo = null;
	        }

	        // 3. ⭐ DB를 먼저 업데이트 (순서 변경!)
	        // 리뷰 테이블의 사진 번호를 먼저 바꾸거나 지워야 외래키 연결이 끊깁니다.
	        reviewDto.setReviewAttachmentNo(finalAttachmentNo);
	        boolean isUpdated = reviewDao.update(reviewDto);

	        // 4. ⭐ DB 업데이트가 성공한 후, 연결이 끊어진 '옛날 사진'을 삭제
	        if (isUpdated && oldAttachmentNo != null) {
	            // 새 파일로 교체되었거나(finalAttachmentNo가 새 번호), 삭제 요청(null)인 경우
	            if (finalAttachmentNo == null || !oldAttachmentNo.equals(finalAttachmentNo)) {
	                attachmentService.delete(oldAttachmentNo);
	            }
	        }

	        return isUpdated;

	    } catch (Exception e) {
	        throw new RuntimeException("리뷰 수정 및 파일 처리 중 오류 발생", e);
	    }
	}

	@Transactional
	public boolean delete(int reviewNo) { // ⭐⭐ memberId 파라미터 추가

		// 1. 리뷰 조회 (파일 번호를 얻기 위해 필수)
		ReviewDto existingReview = reviewDao.selectOne(reviewNo);
		if (existingReview == null) {
			throw new TargetNotFoundException("삭제할 리뷰를 찾을 수 없습니다.");
		}

		Integer attachmentNo = existingReview.getReviewAttachmentNo();

		try {
			// 2. 리뷰 DB 레코드 삭제
			boolean reviewDeleted = reviewDao.delete(reviewNo);

			// 3. 첨부 파일이 있었다면 삭제
			if (attachmentNo != null) {
				attachmentService.delete(attachmentNo);
			}

			return reviewDeleted;

		} catch (Exception e) {
			throw new RuntimeException("리뷰 삭제 및 파일 처리 중 오류 발생", e);
		}
	}
}

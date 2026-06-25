package com.milky.misiklog.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.ReviewDto;
import com.milky.misiklog.vo.ReviewAdminVO;
import com.milky.misiklog.vo.ReviewListVO;

@Repository
public class ReviewDao {

	@Autowired
	private SqlSession sqlSession;

	// 리뷰등록
	public void insert(ReviewDto reviewDto) {
		sqlSession.insert("review.insert", reviewDto);
	}

	// 특정 식당의 리뷰 목록 조회
	public List<ReviewDto> selectListByRestaurant(int restaurantNo) {
		return sqlSession.selectList("review.listByRestaurant", restaurantNo);
	}

	// 특정 사용자가 작성한 리뷰 목록 조회
	public List<ReviewDto> selectListByUser(String memberId) {
		return sqlSession.selectList("review.listByUser", memberId);
	}

	// 리뷰 상세 조회
	public ReviewDto selectOne(int reviewNo) {
		return sqlSession.selectOne("review.detail", reviewNo);
	}

	// 리뷰 수정
	public boolean update(ReviewDto reviewDto) {
		return sqlSession.update("review.update", reviewDto) > 0;
	}

	// 리뷰 삭제
	public boolean delete(int reviewNo) {
		return sqlSession.delete("review.delete", reviewNo) > 0;
	}
	
	//식당별 평균 별점 조회
	public ReviewListVO reviewAvg(long restaurantId) {
		return sqlSession.selectOne("review.reviewAvg", restaurantId);
	}
	
	// 평균 별점 조회
//	public double selectAverageRating(int restaurantId) {
//		Double avg = sqlSession.selectOne("review.selectAverageRating", restaurantId);
//		return avg != null ? avg : 0.0;
//	}
//
//	// 특정 식당 리뷰 개수 조회
//	public int selectReviewCount(int restaurantId) {
//		Integer count = sqlSession.selectOne("review.selectReviewCount", restaurantId);
//		return count != null ? count : 0;
//	}
	
	public List<ReviewAdminVO> selectListAdmin(){
		return sqlSession.selectList("review.selectListAdmin");
	}

}

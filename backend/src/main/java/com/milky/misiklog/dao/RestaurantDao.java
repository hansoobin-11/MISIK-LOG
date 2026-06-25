package com.milky.misiklog.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.RestaurantDto;
import com.milky.misiklog.error.TargetNotFoundException;
import com.milky.misiklog.vo.PageVO;
import com.milky.misiklog.vo.RestaurantDetailVO;
import com.milky.misiklog.vo.RestaurantListVO;
import com.milky.misiklog.vo.RestaurantOwnerDetailVO;
import com.milky.misiklog.vo.SearchVO;

@Repository
public class RestaurantDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public long sequence() {
	    return sqlSession.selectOne("restaurant.sequence");
	}
	
	public RestaurantDto insert(RestaurantDto restaurantDto) {
		long sequence = sqlSession.selectOne("restaurant.sequence");
		restaurantDto.setRestaurantId(sequence);
		sqlSession.insert("restaurant.insert", restaurantDto);
		return restaurantDto;
	}
	
	//단순 상세 조회(데이터 검사용)
	public RestaurantDto selectOne(long restaurantId) {
		RestaurantDto restaurantDto = sqlSession.selectOne("restaurant.detail", restaurantId);
		if(restaurantDto == null) throw new TargetNotFoundException();
		return restaurantDto;
	}
	
	public RestaurantDetailVO selectOneWithCategory(long restaurantId) {
		RestaurantDetailVO restaurantDetailVO = sqlSession.selectOne("restaurant.detailWithCategory", restaurantId);
		if(restaurantDetailVO == null) throw new TargetNotFoundException();
		return restaurantDetailVO;
	}
	//승인 대기 식당 목록
	public List<RestaurantDto> selectApprovalList(PageVO pageVO){
		Map<String, Integer> params = new HashMap<>();
		params.put("begin", pageVO.getBegin());
		params.put("end", pageVO.getEnd());
		return sqlSession.selectList("restaurant.listNeedApprove", params);
	}
	
	//승인 대기 식당 상세
	public RestaurantDto selectFormByAdmin(long restaurantId) {
		RestaurantDto restaurantDto = sqlSession.selectOne("restaurant.detailNeedApprove", restaurantId);
		return restaurantDto;
	}
	
	//식당 주인이 조회할 미승인 식당 목록
	public List<RestaurantDto> selectFormListByOwnerId(String ownerId){
	    return sqlSession.selectList("restaurant.pendingFormList", ownerId);
	}
	
	//식당 주인이 조회할 미승인 식당 상세
	public RestaurantDto selectFormByOwnerId(long restaurantId, String ownerId) {
		Map<String, Object> params = new HashMap<>();
		params.put("restaurantId", restaurantId);
		params.put("ownerId", ownerId);
		RestaurantDto restaurantDto = sqlSession.selectOne("restaurant.pendingFormDetail", params);
		if(restaurantDto == null) throw new TargetNotFoundException();
		return restaurantDto;
	}
	
	//식당 주인 신청서 수정(부분 수정)
	public boolean patchForm(RestaurantDto restaurantDto) {
		return sqlSession.update("restaurant.updateUnitForm", restaurantDto) > 0;
	}
	
	//식당 주인 신청서 주소 수정
	public boolean updateAddress(RestaurantDto restaurantDto) {
		return sqlSession.update("restaurant.updateAddressForm", restaurantDto) > 0;
	}
	
	//카테고리 + 지역그룹 포함 식당 목록
	public List<RestaurantListVO> selectListWithCategory(PageVO pageVO){
		Map<String, Integer> params = new HashMap<>();
		params.put("begin", pageVO.getBegin());
		params.put("end", pageVO.getEnd());
		
		return sqlSession.selectList("restaurant.listWithCategory", params);
	}
	
	//페이지 계산(목록)
	public int listCount() {
		return sqlSession.selectOne("restaurant.listCount");
	}
	
	//페이지 계산(승인해야할 식당 목록)
	public int approvalCount() {
		return sqlSession.selectOne("restaurant.approvalListCount");
	}
	
	//식당 프로필 이미지 등록
	public void connect(long restaurantId, int attachmentNo) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("restaurantId", restaurantId);
		params.put("attachmentNo", attachmentNo);
		
		sqlSession.insert("restaurant.connect", params);
	}
	
	//번호로 파일 찾기
	public int findAttachment(long restaurantId) {
		return sqlSession.selectOne("restaurant.findProfile", restaurantId);
	}
	
	//검색용
	public List<RestaurantListVO> searchList(SearchVO searchVO) {
	    return sqlSession.selectList("restaurant.searchList", searchVO);
	}
	
	//식당이 선택한 카테고리 ID 목록
	public List<Long> selectCategoryIdList(long restaurantId) {
	    return sqlSession.selectList(
	        "restaurant.selectCategoryIdList",
	        restaurantId
	    );
	}
	//사장님 식당 상세 (카테고리 포함)
	public RestaurantOwnerDetailVO selectOwnerDetail(long restaurantId) {

	    RestaurantOwnerDetailVO restaurantOwnerDetailVOvo = sqlSession.selectOne("restaurant.ownerDetail", restaurantId);

	    if (restaurantOwnerDetailVOvo == null) throw new TargetNotFoundException();

	    List<Long> categoryIdList = selectCategoryIdList(restaurantId);
	    restaurantOwnerDetailVOvo.setCategoryIdList(categoryIdList);

	    return restaurantOwnerDetailVOvo;
	}
	//식당 승인
	public boolean approveByAdmin(long restaurantId) {
		return sqlSession.update("restaurant.approveByAdmin", restaurantId) > 0;
	}
	
	//리뷰 등록 시 평균값 수정
	public boolean updateReviewAvg(long restaurantId, double restaurantAvgRating) {
		Map<String, Object> params = new HashMap<>();
		params.put("restaurantId", restaurantId);
		params.put("restaurantAvgRating", restaurantAvgRating);
		
		return sqlSession.update("restaurant.updateReviewAvg", params) > 0;
	}
}

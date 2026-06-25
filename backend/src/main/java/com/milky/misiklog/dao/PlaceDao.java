package com.milky.misiklog.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.PlaceDto;
import com.milky.misiklog.dto.PlaceImageDto;
import com.milky.misiklog.vo.PlaceImageVO;

@Repository
public class PlaceDao {

	@Autowired
	private SqlSession sqlSession;
	
	public long sequence() {
		return sqlSession.selectOne("place.sequence");
	}
	
	public void insert(PlaceDto placeDto) {
		sqlSession.insert("place.insert", placeDto);
	}
	
	// depth별 조회
	public PlaceDto selectByDepth(String depth1, String depth2, String depth3) {
		Map<String, Object> params = new HashMap<>();
		params.put("depth1", depth1);
		params.put("depth2", depth2);
		params.put("depth3", depth3);
		
		return sqlSession.selectOne("place.selectByDepth", params);
	}
	
    // 관리자 지역 매핑용
    public PlaceDto selectPlaceByRestaurantId(Long restaurantId) {
        return sqlSession.selectOne(
            "place.selectPlaceByRestaurantId",
            restaurantId
        );
    }
    public List<String> selectUpperList() {
        return sqlSession.selectList("place.upperPlaceList");
    }

    public List<String> selectLowerList(String upperPlace) {
        return sqlSession.selectList("place.lowerPlaceList", upperPlace);
    }
    
    public List<PlaceDto> selectList() {
        return sqlSession.selectList("place.list");
    }
    
    //지역 이미지 
    public void insertPlaceImage(Long placeId, int attachmentNo) {
        Map<String, Object> param = new HashMap<>();
        param.put("placeId", placeId);
        param.put("attachmentNo", attachmentNo);

        sqlSession.insert("place.insertPlaceImage", param);
    }
    
    //단일 지역 조회
    public PlaceDto selectOne(Long placeId) {
        return sqlSession.selectOne("place.selectOne", placeId);
    }
    
    //depth1별 대표 지역 조회 (이미지 등록용)
    public List<PlaceDto> selectDepth1RepresentativeList() {
        return sqlSession.selectList("place.selectDepth1RepresentativeList");
    }

    // 지역 이미지 attachmentNo 조회
    public Integer selectPlaceImageAttachmentNo(Long placeId) {
        return sqlSession.selectOne(
            "place.selectPlaceImageAttachmentNo",
            placeId
        );
    }
    
    //depth1별 이미지 조회
    public List<PlaceImageVO> selectDepth1WithImage() {
        return sqlSession.selectList("place.selectDepth1WithImage");
    }

    // 지역 이미지 삭제
    public void deletePlaceImage(Long placeId) {
        sqlSession.delete("place.deletePlaceImage", placeId);
    }
}
package com.milky.misiklog.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.PlaceGroupMappingDto;

@Repository
public class PlaceGroupMappingDao {
	
	@Autowired
	private SqlSession sqlSession;
	
	public void insert(PlaceGroupMappingDto dto) {
		sqlSession.insert("placeGroupMapping.insert", dto);
	}
//	매핑 확인
	public boolean selectByMapping(Long placeId, Long placeGroupId) {
		Map<String, Object> params = new HashMap<>();
		params.put("placeId", placeId);
		params.put("placeGroupId", placeGroupId);
		
		Integer count = sqlSession.selectOne(
				"placeGroupMapping.selectByMapping", params
		);
		
		return count != null && count > 0;
	}
}

package com.milky.misiklog.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.PlaceGroupDto;

@Repository
public class PlaceGroupDao {
	@Autowired
	private SqlSession sqlSession;
	
	public long sequence() {
		return sqlSession.selectOne("placeGroup.sequence");
	}
	
	public void insert(PlaceGroupDto placeGroupDto) {
		sqlSession.insert("placeGroup.insert", placeGroupDto);
	}
	
	//이름으로 그룹 찾기
	public PlaceGroupDto selectByName(String depth1) {
		Map<String, Object> params = new HashMap<>();
		params.put("depth1", depth1);
		
		return sqlSession.selectOne("placeGroup.selectByName", params);
	}
	public Long findParentGroupId(Long placeGroupId) {
        return sqlSession.selectOne("placeGroup.findParentGroupId", placeGroupId);
    }
}

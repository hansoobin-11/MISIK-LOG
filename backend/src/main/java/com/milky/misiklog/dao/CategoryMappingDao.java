package com.milky.misiklog.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryMappingDao {

    @Autowired
    private SqlSession sqlSession;

    public void insert(Long restaurantId, Long categoryNo) {
        Map<String, Object> map = new HashMap<>();
        map.put("restaurantId", restaurantId);
        map.put("categoryNo", categoryNo);
        
        sqlSession.insert("categoryMapping.insert", map);
    }
}

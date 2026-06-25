package com.milky.misiklog.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.CategoryDto;
import com.milky.misiklog.vo.CategoryImageVO;

@Repository
public class CategoryDao {

    @Autowired
    private SqlSession sqlSession;
    
    public long sequence() {
        return sqlSession.selectOne("category.sequence");
    }

    // 전체 카테고리 조회 
    public List<CategoryDto> selectList() {
        return sqlSession.selectList("category.list");
    }
    
    // 등록
    public void insert(CategoryDto categoryDto) {
        sqlSession.insert("category.insert", categoryDto);
    }

    // 단일 카테고리 조회
    public CategoryDto selectOne(int categoryNo) {
        return sqlSession.selectOne("category.detail", categoryNo);
    }

    // 수정
    public boolean updateUnit(CategoryDto categoryDto) {
        return sqlSession.update("category.updateUnit", categoryDto) > 0;
    }

    // 삭제
    public boolean delete(int categoryNo) {
        return sqlSession.delete("category.delete", categoryNo) > 0;
    }
    
    // 상위 카테고리 목록
    public List<CategoryDto> selectParentList() {
        return sqlSession.selectList("category.listParent");
    }

    // 하위 카테고리 목록
    public List<CategoryDto> selectChildList(int parentNo) {
        return sqlSession.selectList("category.listByParent", parentNo);
    }
    
    // 상위 카테고리 + 이미지 
    public List<CategoryImageVO> selectTopCategoryWithImage() {
        return sqlSession.selectList(
            "category.selectTopCategoryWithImage"
        );
    }

    // 카테고리 이미지 연결
    public void insertCategoryImage(int categoryNo, int attachmentNo) {
        Map<String, Object> param = new HashMap<>();
        param.put("categoryNo", categoryNo);
        param.put("attachmentNo", attachmentNo);

        sqlSession.insert("category.insertCategoryImage", param);
    }
    
    // 카테고리 이미지의 attachmentNo를 조회
    public Integer selectCategoryImageAttachmentNo(int categoryNo) {
        return sqlSession.selectOne(
            "category.selectCategoryImageAttachmentNo",
            categoryNo
        );
    }

    // 이미지 삭제 
    public void deleteCategoryImage(int categoryNo) {
        sqlSession.delete("category.deleteCategoryImage",categoryNo);
    }

    // 특정 식당이 선택한 카테고리 목록 조회
    public List<CategoryDto> selectCategoryByRestaurant(int restaurantId) {
        return sqlSession.selectList(
            "category.selectCategoryByRestaurant",
            restaurantId
        );
    }

    // 특정 식당의 카테고리 매핑 전체 삭제
    public int deleteRestaurantCategory(int restaurantId) {
        return sqlSession.delete(
            "category.deleteRestaurantCategory",
            restaurantId
        );
    }

    // 특정 식당에 카테고리 1개 매핑 추가
    public int insertRestaurantCategory(int restaurantId, int categoryNo) {
        Map<String, Object> param = new HashMap<>();
        param.put("restaurantId", restaurantId);
        param.put("categoryNo", categoryNo);

        return sqlSession.insert(
            "category.insertRestaurantCategory",
            param
        );
    }
}

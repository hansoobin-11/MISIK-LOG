package com.milky.misiklog.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.RestaurantDto;
import com.milky.misiklog.dto.WishlistDto;

@Repository
public class WishlistDao {

	@Autowired
	private SqlSession sqlSession;

	// 저장하기
	public void insert(WishlistDto wishlistDto) {
		sqlSession.insert("wishlist.insert", wishlistDto);
	}

	// 저장취소
	public boolean delete(WishlistDto wishlistDto) {
		return sqlSession.delete("wishlist.delete", wishlistDto) > 0;
	}

	// 저장 여부 확인
	public boolean check(WishlistDto wishlistDto) {
		int count = sqlSession.selectOne("wishlist.check", wishlistDto);
        return count > 0;
	}

	// 내가 저장한 맛집 개수 조회
	public int countByMember(String memberId) {
		return sqlSession.selectOne("wishlist.countByMember", memberId);
	}

	// 내가 저장한 맛집 목록 조회
	public List<RestaurantDto> selectList(String memberId) {
		return sqlSession.selectList("wishlist.selectList", memberId);
	}
	
	//식당 별 저장 개수 조회
	public int countByRestaurantId(int restaurantId) {
		return sqlSession.selectOne("wishlist.countByRestaurantId", restaurantId);
	}

}

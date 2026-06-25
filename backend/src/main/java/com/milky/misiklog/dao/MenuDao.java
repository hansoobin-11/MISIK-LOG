package com.milky.misiklog.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.MenuDto;

@Repository
public class MenuDao {

    @Autowired
    private SqlSession sqlSession;

    public long sequence() {
        return sqlSession.selectOne("menu.sequence");
    }

    public void insert(MenuDto menuDto) {
        long sequence = sqlSession.selectOne("menu.sequence");
        menuDto.setMenuId(sequence);
        sqlSession.insert("menu.insert", menuDto);
    }

    public List<MenuDto> selectList(long restaurantId) {
        return sqlSession.selectList("menu.list", restaurantId);
    }

    public void update(MenuDto menuDto) {
        sqlSession.update("menu.update", menuDto);
    }

    public void delete(long menuId) {
        sqlSession.delete("menu.delete", menuId);
    }
}

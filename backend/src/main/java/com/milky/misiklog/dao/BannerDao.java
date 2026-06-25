package com.milky.misiklog.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.BannerDto;

@Repository
public class BannerDao {

    @Autowired
    private SqlSession sqlSession;

    public int sequence() {
        return sqlSession.selectOne("banner.sequence");
    }

    public void insert(BannerDto bannerDto) {
        sqlSession.insert("banner.insert", bannerDto);
    }

    public List<BannerDto> selectList() {
        return sqlSession.selectList("banner.list");
    }

    public BannerDto selectOne(int bannerNo) {
        return sqlSession.selectOne("banner.detail", bannerNo);
    }

    public void update(BannerDto bannerDto) {
        sqlSession.update("banner.updateUnit", bannerDto);
    }

    public void delete(int bannerNo) {
        sqlSession.delete("banner.delete", bannerNo);
    }
}

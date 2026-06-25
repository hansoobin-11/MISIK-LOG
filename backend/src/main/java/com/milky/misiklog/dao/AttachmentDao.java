package com.milky.misiklog.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.AttachmentDto;

@Repository
public class AttachmentDao {

    @Autowired
    private SqlSession sqlSession;  

    public int sequence() {
        return sqlSession.selectOne("attachment.sequence");
    }

    public void insert(AttachmentDto dto) {
        sqlSession.insert("attachment.insert", dto);
    }

    public AttachmentDto selectOne(int attachmentNo) {
        return sqlSession.selectOne("attachment.selectOne", attachmentNo);
    }

    public boolean delete(int attachmentNo) {
        return sqlSession.delete("attachment.delete", attachmentNo) > 0;
    }
}

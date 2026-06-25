package com.milky.misiklog.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.vo.MemberProfileVO;

@Repository
public class MemberProfileDao {
	@Autowired
	private SqlSession sqlSession;
	
//	public void insert(String memberId, int memberProfileNo, MemberProfileVO memberProfileVO) {
//		sqlSession.insert("memberProfile.insertProfile", memberProfileVO);
//	}
	
	public MemberProfileVO selectOne(String memberId) {
		return	sqlSession.selectOne("memberProfile.selectAttachmentNoByMemberId", memberId);
	}
	
	public void delete(String memberId) {
		sqlSession.delete("memberProfile.deleteByMemberId", memberId);
	}

	public int insertAttachment(MemberProfileVO vo) {
		return sqlSession.insert("memberProfile.insertAttachment", vo);
		
	}

	public int updateAndInsert(MemberProfileVO vo) {
		return sqlSession.update("memberProfile.updateAndInsertMemberProfile", vo);
	}
	
}

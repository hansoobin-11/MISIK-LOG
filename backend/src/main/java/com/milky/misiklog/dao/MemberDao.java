package com.milky.misiklog.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import com.milky.misiklog.dto.MemberDto;
import com.milky.misiklog.vo.MemberComplexSearchVO;
import com.milky.misiklog.vo.PageVO;

@Repository
public class MemberDao {
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void insert(MemberDto memberDto) {
		String origin = memberDto.getMemberPw();
		String encoded = passwordEncoder.encode(origin); //암호화 작업 수행
		memberDto.setMemberPw(encoded); //암호화한 비번으로 바꿔치기
		
		sqlSession.insert("member.insert", memberDto);
	}
	
	public void insertOwner(MemberDto memberDto) {
		String origin = memberDto.getMemberPw();
		String encoded = passwordEncoder.encode(origin); //암호화 작업 수행
		memberDto.setMemberPw(encoded); //암호화한 비번으로 바꿔치기
		memberDto.setMemberLevel("자영업자"); //자영업자로 만들기
		
		sqlSession.insert("member.insertWithLevel", memberDto);		
	}
	
	public void insertAdmin(MemberDto memberDto) {
		String origin = memberDto.getMemberPw();
		String encoded = passwordEncoder.encode(origin); //암호화 작업 수행
		memberDto.setMemberPw(encoded); //암호화한 비번으로 바꿔치기
		memberDto.setMemberLevel("관리자"); //자영업자로 만들기
		
		sqlSession.insert("member.insertWithLevel", memberDto);		
	}
	
	public MemberDto selectOne(String memberId) {
		return sqlSession.selectOne("member.detail", memberId);
	}

	public MemberDto selectOneBymemberNickname(String memberNickname) {
		return sqlSession.selectOne("member.detailBymemberNickname", memberNickname);
	}
	
	public List<MemberDto> selectList(String column, String keyword) {
		Map<String, Object> params = new HashMap<>();
		params.put("column", column);
		params.put("keyword", keyword);
		return sqlSession.selectList("member.search", params);
	}	
	
	public boolean updateMember(MemberDto memberDto) {
		return sqlSession.update("member.updateMember", memberDto) > 0;
	}
	
	public boolean updateLoginTime(String memberId) {
		return sqlSession.update("member.updateLoginTime", memberId) > 0;
	}
	public boolean updateChangeTime(String memberId) {
		return sqlSession.update("member.updateChangeTime", memberId) > 0;
	}
	public boolean updateWithdraw(String memberId) {
		return sqlSession.update("member.updateWithdraw", memberId) > 0;
	}
	
	//활성화 된 회원 찾기
	public MemberDto selectActiveOne(String memberId) {
		return sqlSession.selectOne("member.findMember", memberId);
	}
	
	//관리자 기능에 추가할것
	public boolean updateReactivate(String memberId) {
		return sqlSession.update("member.updateReactivate", memberId) > 0;
	}
	
	public boolean delete(String memberId) {
		return sqlSession.update("member.delete", memberId) > 0;
	}
	
	//휴먼계정 찾아 아이디+탈퇴시간 반환
	public List<MemberDto> selectDormant() {
		return sqlSession.selectList("member.findDormantMember");
	}
	
	//아이디가 휴먼계정인지 확인
	public MemberDto findDormant(String memberId) {
		return sqlSession.selectOne("member.blockDormant", memberId);
	}
	
	//관리자용 검색
	public List<MemberDto> multiSearchList(@RequestBody MemberComplexSearchVO vo) {
		return sqlSession.selectList("member.complexSearch", vo);
	}

	//관리자용 숫자세기
	public int notAdminCount() {
		return sqlSession.selectOne("member.notAdminListCount");
	}

	//관리자용 리스트
	public List<MemberDto> notAdminList(PageVO pageVO) {
		Map<String, Integer> params = new HashMap<>();
		params.put("begin", pageVO.getBegin());
		params.put("end", pageVO.getEnd());
		return sqlSession.selectList("member.notAdminLIst", params);
	}	
	
}

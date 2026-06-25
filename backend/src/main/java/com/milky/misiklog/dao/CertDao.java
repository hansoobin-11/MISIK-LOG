package com.milky.misiklog.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.milky.misiklog.dto.CertDto;

@Repository
public class CertDao {
	@Autowired
	private SqlSession sqlSession;
//	@Autowired
//	private PasswordEncoder passwordEncoder;
	
	public void insert(CertDto certDto) {
//		String origin = certDto.getCertNumber();
//		String encoded = passwordEncoder.encode(origin); //암호화 작업 수행
//		certDto.setCertNumber(encoded); //암호화한 번호로 바꿔치기
		
		sqlSession.insert("cert.insert", certDto);
	}
	
	//업데이트
	public boolean update(CertDto certDto) {
		return sqlSession.update("cert.update", certDto) > 0;
	}
	//삭제
	public boolean delete(String certEmail) { 
		return sqlSession.delete( "cert.delete", certEmail) > 0;
	}
	
	//상세 조회
	public CertDto selectOne(String certEmail) {
		return sqlSession.selectOne( "cert.detail", certEmail);
	}
}

package com.milky.misiklog.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.milky.misiklog.dao.MemberProfileDao;
import com.milky.misiklog.vo.MemberProfileVO;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberProfileService {
	@Autowired
	private MemberProfileDao memberProfileDao; 
	@Autowired
	private AttachmentService attachmentService;
	
	
	//서비스서 할 일은 단 하나! 수정 경로를 만드는것
	
	@Transactional
	public void saveProfile(MemberProfileVO vo, MultipartFile attach, String memberId) throws IllegalStateException, IOException {
		log.info("VO 데이터 확인: {}", vo);
		
		vo.setMemberId(memberId);
		MemberProfileVO oldProfile = memberProfileDao.selectOne(memberId);

		// 1. 기존 프로필이 존재하는지 확인
	    if (oldProfile != null && oldProfile.getAttachmentNo() > 0) {
	        // 기존 프로필 파일이 있으면 삭제
	        attachmentService.delete(oldProfile.getAttachmentNo());
	    }		
		//2. 파일 저장
	    if(attach != null && !attach.isEmpty() ) {	    	

	    	int	attachmentNo = attachmentService.save(attach);
	    	//3. vo에 주입
	    	vo.setAttachmentNo(attachmentNo);
	    	// 4. MERGE 실행 (MEMBER_PROFILE 테이블)
	    	memberProfileDao.updateAndInsert(vo);
	    	
	    	log.info("프로필 변경 완료: 회원={}, 새 파일번호={}", memberId, attachmentNo);
	    }
	}
	
	
	
		
	
}

package com.milky.misiklog.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.milky.misiklog.dao.AttachmentDao;
import com.milky.misiklog.dto.AttachmentDto;
import com.milky.misiklog.error.TargetNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AttachmentService {
    
    private static final Logger log = LoggerFactory.getLogger(AttachmentService.class);
    
	@Autowired
	private AttachmentDao attachmentDao;
	
	private final String uploadPath = System.getProperty("user.home") + "/upload";
	private File upload = new File(uploadPath); 
	
	@Transactional
	public int save(MultipartFile attach) throws IllegalStateException, IOException {
		int attachmentNo = attachmentDao.sequence(); 
		
		if(upload.exists() == false) {
			upload.mkdirs(); 
		}
		
		File target = new File(upload, String.valueOf(attachmentNo)); 
		attach.transferTo(target); 
		
		// DB에 저장된 파일의 정보를 기록
		AttachmentDto attachmentDto = new AttachmentDto();
		attachmentDto.setAttachmentNo(attachmentNo); 
		attachmentDto.setAttachmentName(attach.getOriginalFilename()); 
		attachmentDto.setAttachmentType(attach.getContentType()); 
		attachmentDto.setAttachmentSize(attach.getSize()); 
		
		attachmentDao.insert(attachmentDto);
		
		return attachmentNo; 
	}

	public ByteArrayResource load(int attachmentNo) throws IOException {
		File target = new File(upload, String.valueOf(attachmentNo));
		if(target.isFile() == false) throw new TargetNotFoundException("존재하지 않는 파일");
		
		byte[] data = Files.readAllBytes(target.toPath());
		ByteArrayResource resource = new ByteArrayResource(data);
		
		return resource;
	}
	

	@Transactional
	public void delete(int attachmentNo) {
		AttachmentDto attachmentDto = attachmentDao.selectOne(attachmentNo);
		if(attachmentDto == null) {
		    log.warn("삭제하려는 첨부 파일 번호({})가 DB에 존재하지 않습니다.", attachmentNo);
		    throw new TargetNotFoundException("존재하지 않는 파일");
		}
		
		File target = new File(upload, String.valueOf(attachmentNo));
		boolean physicalDeleted = target.delete();
		
		if (physicalDeleted) {
		    log.info("첨부 파일({})의 물리적 파일({}) 삭제 성공.", attachmentNo, target.getAbsolutePath());
		} else {
		    log.warn("첨부 파일({})의 물리적 파일({}) 삭제 실패 또는 파일이 이미 존재하지 않음.", attachmentNo, target.getAbsolutePath());
		}
	
		attachmentDao.delete(attachmentNo);
        log.info("첨부 파일({}) DB 레코드 삭제 완료.", attachmentNo);
	}
}
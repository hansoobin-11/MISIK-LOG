package com.milky.misiklog.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.milky.misiklog.dao.MemberProfileDao;
import com.milky.misiklog.service.AttachmentService;
import com.milky.misiklog.service.MemberProfileService;
import com.milky.misiklog.service.TokenService;
import com.milky.misiklog.vo.MemberProfileVO;
import com.milky.misiklog.vo.TokenVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

//(origins = "http://localhost:5173") 잘 안되면 이거 밑에 삽입할것
@Tag(name="멤버 프로필 컨트롤러")
@RestController
@Slf4j
@RequestMapping("/memberProfile")
@CrossOrigin
public class MemberProfileRestController {
	@Autowired
	private MemberProfileDao memberProfileDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private MemberProfileService memberProfileService;
	
    private void checkLogin(String bearerToken) {
        tokenService.parse(bearerToken);
    }
    
    @PostMapping("/add")
    public void add(
            @ModelAttribute MemberProfileVO memberProfileVO,
            @RequestParam(required = false) MultipartFile attach,
            @RequestHeader(value = "Authorization", required = false) String bearerToken
    ) throws IllegalStateException, IOException {
        // 1. 로그인 검사 및 토큰에서 아이디 추출
        checkLogin(bearerToken);
        String memberId = tokenService.parse(bearerToken).getLoginId();
        
        // 2. 파일이 전송되었을 때만 서비스 호출 (방어 코드)
        if(attach != null && !attach.isEmpty()) {
            // 모든 로직(기존 삭제, 새 파일 저장, DB 매칭)을 서비스에서 처리하도록 전달
            memberProfileService.saveProfile(memberProfileVO, attach, memberId);
        } else {
            // 파일이 없을 경우에 대한 처리 (생략 가능 혹은 안내 메시지)
            log.warn("업로드된 파일이 없습니다.");
        }
    }
    
    @GetMapping("/{memberId}")
    public MemberProfileVO detail(@PathVariable String memberId) {
    	MemberProfileVO profile = memberProfileDao.selectOne(memberId);
    	return profile;
    }
    
}

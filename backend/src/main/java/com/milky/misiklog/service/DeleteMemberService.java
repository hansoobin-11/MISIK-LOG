package com.milky.misiklog.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.milky.misiklog.dao.MemberDao;
import com.milky.misiklog.dto.MemberDto;

@Service
public class DeleteMemberService {
	@Autowired
	private MemberDao memberDao;
	
	//매월 1일 12시 30분 18시 30분 실행되는 코드
	//@Scheduled(cron = "0 30 12,18 1 * *")
//	@Scheduled(cron = "0 30 12-18 * * *")
	//테스트용 매 5분마다 실행
	@Scheduled(cron = "0 0/5 10-18 * * *")
	public void work() {
		System.out.println(LocalDateTime.now() + "에 실행됨!");
		LocalDateTime current = LocalDateTime.now();
		List<MemberDto> DormantList = memberDao.selectDormant();
		for(MemberDto member: DormantList) {
			LocalDateTime withdrawTime = member.getMemberWithdrawTime();
			Duration duration = Duration.between(withdrawTime, current);
			//long diffSeconds = duration.getSeconds();
			long diffDays = duration.toDays();
			//60초 된 계정 날리기
			//if(diffSeconds > 60L) {
			//60일 된 계정 날리기
			if(diffDays > 60L) {
				memberDao.delete(member.getMemberId());
			}
		}
		
	}
}

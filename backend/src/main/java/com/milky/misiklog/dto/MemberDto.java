package com.milky.misiklog.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"memberPw"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberDto {
	
	@NotBlank
	@Pattern(regexp = "^[a-z][a-z0-9]{4,19}$")
	private String memberId;
	
	@NotBlank
	@Pattern(regexp = "(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[\\\\!\\\\@\\\\#\\\\$])[A-Za-z0-9\\\\!\\\\@\\\\#\\\\$]{8,16}$")
	private String memberPw;

	
	@NotBlank
	@Pattern(regexp = "^[가-힣0-9]{2,10}$")
	private String memberNickname;
	
	@Pattern(regexp = "^(19[0-9]{2}|20[0-9]{2})-((02-(0[1-9]|1[0-9]|2[0-9]))|((0[469]|11)-(0[1-9]|1[0-9]|2[0-9]|30))|((0[13578]|1[02])-(0[1-9]|1[0-9]|2[0-9]|3[01])))$")
	private String memberBirth;
	
	@Pattern(regexp = "^010[1-9][0-9]{7}$")
	private String memberContact;
	
	@NotBlank
	@Email
	private String memberEmail;
	
	//조건 있음 일반회원 자영업자 관리자
	@Pattern(regexp = "^(일반회원|자영업자|관리자)$")
	private String memberLevel;

	
	@Pattern(regexp = "^[0-9]{5,6}$")
	private String memberPost;
	//나머지 주소는 조건이 없음
	@Size(max = 100)
	private String memberAddress1;
	@Size(max = 100)
	private String memberAddress2;
	//Timestamp 대신 LocalDateTime을 써도 무방하다 (mybatis가 자동변환)
	//주의 할것 아래 포멧은 JSON 이 T가 없어도 되지만 시간 사이에 'T'를 넣는것이 표준 API 통신이나 웹 서비스에서 일반적으로 더 권장됨
//	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
//	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime memberJoin;
//	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
//	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime memberLogin;
//	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
//	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime memberChange;	
	
	//조건 있음 active(현재 회원) dormant(휴먼) withdrawn(탈퇴) 
	//추후 enum으로 처리하는것이 옳은 해결책으로 보임 사용법은 클래스 생성 후 import하면 됨
	@Pattern(regexp = "^(active|dormant|withdraw)$")
	private String memberStatus;
	
	//멤버탈퇴 시간
//	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
//	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
//	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private LocalDateTime memberWithdrawTime;	
}

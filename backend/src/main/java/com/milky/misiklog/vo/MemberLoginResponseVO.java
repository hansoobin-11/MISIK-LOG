package com.milky.misiklog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MemberLoginResponseVO {
	private String loginId; //로그인한 사용자의 아이디
	private String loginLevel; //로그인한 사용자의 등급
	private String accessToken; //나중에 사용자가 들고올 토큰
	private String refreshToken; //accessToken에 문제가 있을 때 갱신할 토큰
}

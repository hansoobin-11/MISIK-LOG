package com.milky.misiklog.vo;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown =  true)
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MemberComplexSearchVO {
	private String memberId;
	private String memberNickname;
	private String memberEmail;
	private String memberContact;
	private String memberBirth;
	private String beginMemberJoin, endMemberJoin;
	private List<String> memberLevelList;
	private String memberLevel;
	private String memberAddress;
	private List<String> memberStatusList;
	private String memberStatus;
	
	public Set<String> getAddressTokenList() {
		if(memberAddress == null) return null;

		//	if(accountAddress.matches("^\\s+$")) return null;
		String stripResult = memberAddress.strip();
		if(stripResult.isEmpty()) return null;
		
		String[] tokens = stripResult.split("\\s+"); //분할
		//String[] -> Set<String>
		Set<String> set = Arrays.stream(tokens).collect(Collectors.toSet());
		
		
		return set;
	}
	
	
}

package com.milky.misiklog.vo;

import java.util.List;

import com.milky.misiklog.dto.MemberDto;
import com.milky.misiklog.dto.RestaurantDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MemberNotAdminListVO {
	private int page; //요청한 페이지 번호
	private int count; //전체 데이터 개수
	private int size; //요청한 데이터 개수
	private int begin, end; //현재 요청한 페이지에 대한 행 값
	private boolean last; //마지막 여부
	private List<MemberDto> list;
}

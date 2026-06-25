package com.milky.misiklog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class MemberProfileVO {
	private String memberId;
    private int attachmentNo;
    private String attachmentName;
    private String attachmentType;
}

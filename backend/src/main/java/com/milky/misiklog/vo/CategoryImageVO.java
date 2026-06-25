package com.milky.misiklog.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor@AllArgsConstructor@Builder
	public class CategoryImageVO {

	    private Long categoryNo;
	    private String categoryName;
	    private Integer categoryOrder;
	    private Long attachmentNo;
	    private String attachmentName;
	    private String attachmentType;
	}

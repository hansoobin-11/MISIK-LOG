package com.milky.misiklog.vo;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @Builder @NoArgsConstructor
public class ReviewAdminVO {
	private int reviewNo;
    private int restaurantId;
    private String memberId;
    private String reviewContent;
    private double reviewRating;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime reviewCreatedAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime reviewUpdatedAt;
    private Integer reviewAttachmentNo;
    private String restaurantName;
    private int reportCount;
}

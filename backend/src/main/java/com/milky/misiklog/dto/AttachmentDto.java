package com.milky.misiklog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttachmentDto {
    private Integer attachmentNo;
    private String attachmentName;
    private long attachmentSize;
    private String attachmentType;
    private java.sql.Timestamp attachmentTime;

}


package com.milky.misiklog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PlaceImageDto {
    private Long placeId;        
    private Long attachmentNo;   
}

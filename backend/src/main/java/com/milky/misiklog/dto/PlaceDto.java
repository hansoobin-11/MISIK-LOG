package com.milky.misiklog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDto {
    private Long placeId;
    private String placeDepth1;
    private String placeDepth2;
    private String placeDepth3;
}

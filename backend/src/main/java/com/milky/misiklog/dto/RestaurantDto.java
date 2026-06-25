package com.milky.misiklog.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantDto {
    private Long restaurantId;
    private String ownerId;
    private Long restaurantPlace;
    private String restaurantName;
    private String restaurantOpen;
    private String restaurantClose;
    private String restaurantBreakStart;
    private String restaurantBreakEnd;
    private String restaurantLastOrder; 
    private Integer reservationInterval;
    private String restaurantContact;
    private String restaurantAddress;
    private Double restaurantAddressX;
    private Double restaurantAddressY;
    private String restaurantDescription;
    private String restaurantOpeningDays;
    private String restaurantStatus;
    private Double restaurantAvgRating;
    private Integer restaurantReservationPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime restaurantCreatedAt;
}

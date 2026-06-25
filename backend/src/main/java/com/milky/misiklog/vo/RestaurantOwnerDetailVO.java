package com.milky.misiklog.vo;

import java.util.List;

import lombok.Data;

@Data
public class RestaurantOwnerDetailVO {

    private Long restaurantId;
    private String restaurantName;
    private String restaurantContact;
    private String restaurantAddress;
    private Double restaurantAddressX;
    private Double restaurantAddressY;
    private String restaurantDescription;

    private String restaurantOpen;
    private String restaurantClose;
    private String restaurantBreakStart;
    private String restaurantBreakEnd;
    private String restaurantLastOrder;
    private String restaurantOpeningDays;

    private Integer reservationInterval;
    private Integer restaurantReservationPrice;

    private List<Long> categoryIdList;
}



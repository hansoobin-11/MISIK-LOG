package com.milky.misiklog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.milky.misiklog.dao.CategoryMappingDao;
import com.milky.misiklog.dao.RestaurantDao;
import com.milky.misiklog.dao.SeatDao;
import com.milky.misiklog.dto.RestaurantDto;
import com.milky.misiklog.dto.SeatDto;
import com.milky.misiklog.vo.RestaurantRegisterVO;
import com.milky.misiklog.vo.SeatVO;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private CategoryMappingDao categoryMappingDao; 

    @Autowired
    private SeatDao seatDao;
    
        @Transactional
        public long createRestaurant(RestaurantRegisterVO restaurantRegisterVO) {
            
        	String address = restaurantRegisterVO.getRestaurantAddress();
            long placeId = placeService.createPlaceAndMapping(address);
            restaurantRegisterVO.setRestaurantPlace(placeId);

            RestaurantDto dto = RestaurantDto.builder()
                    .ownerId(restaurantRegisterVO.getOwnerId())
                    .restaurantPlace(restaurantRegisterVO.getRestaurantPlace())
                    .restaurantName(restaurantRegisterVO.getRestaurantName())
                    .restaurantOpen(restaurantRegisterVO.getRestaurantOpen())
                    .restaurantClose(restaurantRegisterVO.getRestaurantClose())
                    .restaurantBreakStart(restaurantRegisterVO.getRestaurantBreakStart())
                    .restaurantBreakEnd(restaurantRegisterVO.getRestaurantBreakEnd())
                    .restaurantLastOrder(restaurantRegisterVO.getRestaurantLastOrder())
                    .reservationInterval(restaurantRegisterVO.getReservationInterval())
                    .restaurantContact(restaurantRegisterVO.getRestaurantContact())
                    .restaurantAddress(restaurantRegisterVO.getRestaurantAddress())
                    .restaurantAddressX(restaurantRegisterVO.getRestaurantAddressX())
                    .restaurantAddressY(restaurantRegisterVO.getRestaurantAddressY())
                    .restaurantDescription(restaurantRegisterVO.getRestaurantDescription())
                    .restaurantOpeningDays(restaurantRegisterVO.getRestaurantOpeningDays())
                    .build();

            RestaurantDto result = restaurantDao.insert(dto);
            long restaurantId = result.getRestaurantId();

            // 카테고리 매핑 등록
               
            for (Long categoryNo : restaurantRegisterVO.getCategoryIdList()) {
                    categoryMappingDao.insert(restaurantId, categoryNo);
                }
            
            // 좌석 등록
            for(SeatVO seatVO : restaurantRegisterVO.getSeatList()) {
            	int count = seatVO.getCount();
            	SeatDto seatDto = seatVO.getSeatDto();
            	seatDto.setSeatRestaurantId(restaurantId);
            	for(int i = 0 ; i < count; i++) {
        			seatDao.insert(seatDto);
        		}
            }
            
            return restaurantId;
    }
    
    //주소 수정 처리
    @Transactional
    public RestaurantDto updateRestaurantPlace(RestaurantDto restaurantDto) {
    	String address = restaurantDto.getRestaurantAddress();
    	long placeId = placeService.createPlaceAndMapping(address);
    	restaurantDto.setRestaurantPlace(placeId);
    	
    	restaurantDto = RestaurantDto.builder()
    		.restaurantAddress(restaurantDto.getRestaurantAddress())
    		.restaurantAddressX(restaurantDto.getRestaurantAddressX())
    		.restaurantAddressY(restaurantDto.getRestaurantAddressY())
    	.build();
    	
    	return restaurantDto;
    }
    
}
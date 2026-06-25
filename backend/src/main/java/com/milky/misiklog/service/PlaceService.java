package com.milky.misiklog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.milky.misiklog.dao.PlaceDao;
import com.milky.misiklog.dao.PlaceGroupDao;
import com.milky.misiklog.dao.PlaceGroupMappingDao;
import com.milky.misiklog.dto.PlaceDto;
import com.milky.misiklog.dto.PlaceGroupDto;
import com.milky.misiklog.dto.PlaceGroupMappingDto;
import com.milky.misiklog.vo.PlaceGroupMappingRequestVO;

@Service
public class PlaceService {
	@Autowired
	private PlaceDao placeDao;
	
	@Autowired
	private PlaceGroupDao placeGroupDao;
	
	@Autowired
	private PlaceGroupMappingDao placeGroupMappingDao;
	
	@Transactional
	public long createPlaceAndMapping(String address) {
	    String[] tokens = address.split(" "); //공백 기준으로 자르기
	    
	    String depth1 = tokens.length > 0 ? tokens[0] : null;
	    String depth2 = tokens.length > 1 ? tokens[1] : null;
	    String depth3 = tokens.length > 2 ? tokens[2] : null;
	    
	    if(depth1 != null) {
            depth1 = depth1.replaceAll("(특별자치도|특별자치시|특별시|광역시|도)$", "");
        }
	    
	    // 1. place 조회 또는 생성
	    PlaceDto place = placeDao.selectByDepth(depth1, depth2, depth3);
	    long placeId;
	    if (place == null) {
	        placeId = placeDao.sequence();
	        place = PlaceDto.builder()
	                .placeId(placeId)
	                .placeDepth1(depth1)
	                .placeDepth2(depth2)
	                .placeDepth3(depth3)
	                .build();
	        placeDao.insert(place);
	    } else {
	        placeId = place.getPlaceId();
	    }
	    // 2. depth1 그룹(예: "서울") 조회 또는 생성
	    PlaceGroupDto depth1Group = placeGroupDao.selectByName(depth1);
	    long depth1GroupId;
	    if (depth1Group == null) {
	        depth1GroupId = placeGroupDao.sequence();
	        depth1Group = PlaceGroupDto.builder()
	                .placeGroupId(depth1GroupId)
	                .placeGroupName(depth1)
	                .parentGroupId(null)
	                .build();
	        placeGroupDao.insert(depth1Group);
	    } else {
	        depth1GroupId = depth1Group.getPlaceGroupId();
	    }
	    // 3. depth1 전체 그룹("서울 전체") 조회 또는 생성
	    String wholeGroupName = depth1 + " 전체";
	    PlaceGroupDto wholeGroup = placeGroupDao.selectByName(wholeGroupName);
	    long wholeGroupId;
	    if (wholeGroup == null) {
	        wholeGroupId = placeGroupDao.sequence();
	        wholeGroup = PlaceGroupDto.builder()
	                .placeGroupId(wholeGroupId)
	                .placeGroupName(wholeGroupName)
	                .parentGroupId(depth1GroupId)
	                .build();
	        placeGroupDao.insert(wholeGroup);
	    } else {
	        wholeGroupId = wholeGroup.getPlaceGroupId();
	    }
	    // 4. 매핑
	    boolean hasResult = placeGroupMappingDao.selectByMapping(placeId, depth1GroupId);
	    if(!hasResult) {
	    	PlaceGroupMappingDto mapping1 = PlaceGroupMappingDto.builder()
		    		.placeGroupId(depth1GroupId)
		    		.placeId(placeId)
		    		.build();
		    placeGroupMappingDao.insert(mapping1);
	    }
	    boolean hasResult2 = placeGroupMappingDao.selectByMapping(placeId, wholeGroupId);
	    if(!hasResult2) {
	    	PlaceGroupMappingDto mapping2 = PlaceGroupMappingDto.builder()
		            .placeGroupId(wholeGroupId)
		            .placeId(placeId)
		            .build();
		    placeGroupMappingDao.insert(mapping2);
	    }

	    return placeId;
	}
	
	@Transactional
    public void createMapping(PlaceGroupMappingRequestVO placeGroupMappingRequestVO) {
        List<Long> restaurantIds = placeGroupMappingRequestVO.getRestaurantIds();
        Long placeGroupId = placeGroupMappingRequestVO.getPlaceGroupId();
        for (Long restaurantId : restaurantIds) {
            PlaceDto place = placeDao.selectPlaceByRestaurantId(restaurantId);
            if (place == null) continue;
            Long placeId = place.getPlaceId();
            mappingWithParents(placeId, placeGroupId);
        }
    }
	
    private void mappingWithParents(Long placeId, Long placeGroupId) {
        Long currentGroupId = placeGroupId;
        while (currentGroupId != null) {
            boolean exists =
                placeGroupMappingDao.selectByMapping(placeId, currentGroupId);
            if (!exists) {
                placeGroupMappingDao.insert(
                    PlaceGroupMappingDto.builder()
                        .placeId(placeId)
                        .placeGroupId(currentGroupId)
                        .build()
                );
            }
         
            currentGroupId = placeGroupDao.findParentGroupId(currentGroupId);
        }
    }
}


package com.milky.misiklog.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.milky.misiklog.dao.RestaurantDao;
import com.milky.misiklog.dto.RestaurantDto;
import com.milky.misiklog.error.TargetNotFoundException;
import com.milky.misiklog.service.RestaurantService;
import com.milky.misiklog.vo.TokenVO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/owner")
public class RestaurantOwnerRestController {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private RestaurantService restaurantService;

     //내가 등록한 식당 목록
    @GetMapping("/list")
    public List<RestaurantDto> myRestaurantList(HttpServletRequest request) {

        TokenVO tokenVO = (TokenVO) request.getAttribute("tokenVO");
        if (tokenVO == null) {
            throw new TargetNotFoundException("로그인 정보 없음");
        }

        String ownerId = tokenVO.getLoginId();
        log.info("MY RESTAURANT ownerId = {}", ownerId);

        return restaurantDao.selectFormListByOwnerId(ownerId);
    }

     //내가 등록한 식당 상세
    @GetMapping("/{restaurantId}")
    public RestaurantDto myRestaurantDetail(
            HttpServletRequest request,
            @PathVariable long restaurantId
    ) {
        TokenVO tokenVO = (TokenVO) request.getAttribute("tokenVO");
        if (tokenVO == null) {
            throw new TargetNotFoundException("로그인 정보 없음");
        }

        RestaurantDto dto = restaurantDao.selectFormByOwnerId(
                restaurantId,
                tokenVO.getLoginId()
        );

        if (dto == null) {
            throw new TargetNotFoundException("식당이 없거나 권한 없음");
        }

        return dto;
    }

     //주소 수정
    @PutMapping("/edit/address/{restaurantId}")
    public RestaurantDto updateAddress(
            HttpServletRequest request,
            @PathVariable long restaurantId,
            @RequestBody RestaurantDto restaurantDto
    ) {
        TokenVO tokenVO = (TokenVO) request.getAttribute("tokenVO");
        if (tokenVO == null) {
            throw new TargetNotFoundException("로그인 정보 없음");
        }

        restaurantDto.setRestaurantId(restaurantId);
        restaurantDto.setOwnerId(tokenVO.getLoginId());

        return restaurantService.updateRestaurantPlace(restaurantDto);
    }

     //식당 정보 부분 수정
    @PatchMapping("/edit/{restaurantId}")
    public void patchRestaurant(
            HttpServletRequest request,
            @PathVariable long restaurantId,
            @RequestBody RestaurantDto restaurantDto
    ) {
        TokenVO tokenVO = (TokenVO) request.getAttribute("tokenVO");
        if (tokenVO == null) {
            throw new TargetNotFoundException("로그인 정보 없음");
        }

        restaurantDto.setRestaurantId(restaurantId);
        restaurantDto.setOwnerId(tokenVO.getLoginId());

        boolean success = restaurantDao.patchForm(restaurantDto);
        if (!success) {
            throw new TargetNotFoundException("수정 실패 또는 권한 없음");
        }
    }
}

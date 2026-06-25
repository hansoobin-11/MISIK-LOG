package com.milky.misiklog.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.milky.misiklog.dao.MenuDao;
import com.milky.misiklog.dto.MenuDto;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "식당 메뉴 관리 컨트롤러")
@RestController
@RequestMapping("/owner/restaurant")
@CrossOrigin
public class RestaurantMenuRestController {

    @Autowired
    private MenuDao menuDao;

    // 메뉴 등록
    @PostMapping("/{restaurantId}/menu")
    public void insert(
        @PathVariable long restaurantId,
        @RequestBody MenuDto menuDto
    ) {
        menuDto.setRestaurantId(restaurantId);
        menuDao.insert(menuDto);
    }

    // 메뉴 목록 조회 (관리 화면용)
    @GetMapping("/{restaurantId}/menu")
    public List<MenuDto> list(
        @PathVariable long restaurantId
    ) {
        return menuDao.selectList(restaurantId);
    }

    // 메뉴 수정
    @PutMapping("/{restaurantId}/menu/{menuId}")
    public void update(
        @PathVariable long restaurantId,
        @PathVariable long menuId,
        @RequestBody MenuDto menuDto
    ) {
        menuDto.setMenuId(menuId);
        menuDto.setRestaurantId(restaurantId);
        menuDao.update(menuDto);
    }

    // 메뉴 삭제 
    @DeleteMapping("/{restaurantId}/menu/{menuId}")
    public void delete(
        @PathVariable long restaurantId,
        @PathVariable long menuId
    ) {
        menuDao.delete(menuId);
    }
}

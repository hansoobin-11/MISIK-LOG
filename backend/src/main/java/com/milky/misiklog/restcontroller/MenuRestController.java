package com.milky.misiklog.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.milky.misiklog.dao.MenuDao;
import com.milky.misiklog.dto.MenuDto;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "메뉴 관리 컨트롤러")
@RestController
@RequestMapping("/menu")
@CrossOrigin
public class MenuRestController {

    @Autowired
    private MenuDao menuDao;

    // 메뉴 등록
    @PostMapping("/")
    public void insert(@RequestBody MenuDto menuDto) {
        menuDao.insert(menuDto);
    }

    // 식당별 메뉴 목록 조회
    @GetMapping("/list/{restaurantId}")
    public List<MenuDto> list(@PathVariable long restaurantId) {
        return menuDao.selectList(restaurantId);
    }

    // 메뉴 수정
    @PutMapping("/{menuId}")
    public void update(
        @PathVariable long menuId,
        @RequestBody MenuDto menuDto
    ) {
        menuDto.setMenuId(menuId);
        menuDao.update(menuDto);
    }

    // 메뉴 삭제
    @DeleteMapping("/{menuId}")
    public void delete(@PathVariable long menuId) {
        menuDao.delete(menuId);
    }
}
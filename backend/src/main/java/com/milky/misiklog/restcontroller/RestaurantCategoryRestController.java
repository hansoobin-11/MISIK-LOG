package com.milky.misiklog.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.milky.misiklog.dao.CategoryDao;
import com.milky.misiklog.dto.CategoryDto;
import com.milky.misiklog.service.TokenService;
import com.milky.misiklog.vo.RestaurantCategorySaveVO;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "식당-카테고리 매핑 컨트롤러")
@RestController
@RequestMapping("/owner/restaurant")
@CrossOrigin
public class RestaurantCategoryRestController {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private TokenService tokenService;

    // 🔐 공통 로그인 체크
    private void checkLogin(String bearerToken) {
        tokenService.parse(bearerToken);
    }

    /**
     * ✅ 이 식당이 선택한 카테고리 목록 조회
     * GET /owner/restaurant/{restaurantId}/category
     */
    @GetMapping("/{restaurantId}/category")
    public List<CategoryDto> getRestaurantCategory(
        @PathVariable int restaurantId,
        @RequestHeader("Authorization") String bearerToken
    ) {
        checkLogin(bearerToken);

        return categoryDao.selectCategoryByRestaurant(restaurantId);
    }

    @PostMapping("/{restaurantId}/category")
    public void saveRestaurantCategory(
        @PathVariable int restaurantId,
        @RequestBody RestaurantCategorySaveVO vo,
        @RequestHeader("Authorization") String bearerToken
    ) {
        checkLogin(bearerToken);

        // 기존 매핑 전체 삭제
        categoryDao.deleteRestaurantCategory(restaurantId);

        // 새 매핑 등록
        if (vo != null && vo.getCategoryIdList() != null) {
            for (Integer categoryNo : vo.getCategoryIdList()) {
                if (categoryNo != null) {
                    categoryDao.insertRestaurantCategory(restaurantId, categoryNo);
                }
            }
        }
    }
}

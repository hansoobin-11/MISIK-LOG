package com.milky.misiklog.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.milky.misiklog.dao.CategoryDao;
import com.milky.misiklog.dto.CategoryDto;
import com.milky.misiklog.error.TargetNotFoundException;
import com.milky.misiklog.vo.CategoryImageVO;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "카테고리 컨트롤러")
@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryRestController {

    @Autowired
    private CategoryDao categoryDao;

    // 전체 카테고리 목록 조회
    @GetMapping("/")
    public List<CategoryDto> list() {
        return categoryDao.selectList();
    }

    // 단일 카테고리 조회
    @GetMapping("/{categoryNo}")
    public CategoryDto detail(@PathVariable int categoryNo) {
        CategoryDto categoryDto = categoryDao.selectOne(categoryNo);

        if (categoryDto == null)
            throw new TargetNotFoundException("존재하지 않는 카테고리 번호");

        return categoryDto;
    }

    // 상위 카테고리 목록 조회
    @GetMapping("/parent")
    public List<CategoryDto> getParentList() {
        return categoryDao.selectParentList();
    }

    // 하위 카테고리 목록 조회
    @GetMapping("/child/{parentNo}")
    public List<CategoryDto> getChildList(@PathVariable int parentNo) {
        return categoryDao.selectChildList(parentNo);
    }

    // 홈 화면용 카테고리 + 이미지 조회
    @GetMapping("/top")
    public List<CategoryImageVO> topCategory() {
        return categoryDao.selectTopCategoryWithImage();
    }
}

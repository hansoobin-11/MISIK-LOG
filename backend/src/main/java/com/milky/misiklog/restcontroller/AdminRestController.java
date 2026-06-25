package com.milky.misiklog.restcontroller;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.milky.misiklog.dao.BannerDao;
import com.milky.misiklog.dao.CategoryDao;
import com.milky.misiklog.dao.MemberDao;
import com.milky.misiklog.dao.PlaceDao;
import com.milky.misiklog.dao.RestaurantDao;
import com.milky.misiklog.dao.ReviewDao;
import com.milky.misiklog.dto.BannerDto;
import com.milky.misiklog.dto.CategoryDto;
import com.milky.misiklog.dto.MemberDto;
import com.milky.misiklog.dto.RestaurantDto;
import com.milky.misiklog.error.TargetNotFoundException;
import com.milky.misiklog.error.UnauthorizationException;
import com.milky.misiklog.service.AttachmentService;
import com.milky.misiklog.vo.MemberComplexSearchVO;
import com.milky.misiklog.vo.PageVO;
import com.milky.misiklog.vo.RestaurantApprovalListVO;
import com.milky.misiklog.vo.ReviewAdminVO;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "관리자 기능 컨트롤러")
@Slf4j
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://192.168.20.12:5173")
public class AdminRestController {

    @Autowired
    private RestaurantDao restaurantDao;

    @Autowired
    private BannerDao bannerDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private ReviewDao reviewDao;
    
    @Autowired
    private PlaceDao placeDao;
    
    @Autowired
    private MemberDao memberDao;
    
	@Autowired
	private SqlSession sqlSession;

    // 승인 안된 식당 리스트
    @GetMapping("/page/{page}")
    public RestaurantApprovalListVO approvalList(@PathVariable int page) {
        PageVO pageVO = new PageVO();
        pageVO.setPage(page);
        pageVO.setDataCount(restaurantDao.approvalCount());

        List<RestaurantDto> list = restaurantDao.selectApprovalList(pageVO);

        return RestaurantApprovalListVO.builder()
                .page(pageVO.getPage())
                .size(pageVO.getSize())
                .count(pageVO.getDataCount())
                .begin(pageVO.getBegin())
                .end(pageVO.getEnd())
                .last(pageVO.getPage() >= pageVO.getTotalPage())
                .list(list)
                .build();
    }

	// 상세 조회
	@GetMapping("/restaurant/{restaurantId}")
	public RestaurantDto detail(@PathVariable long restaurantId) {
		return restaurantDao.selectFormByAdmin(restaurantId);
	}
	
	//관리자용  식당 승인
	@PatchMapping("/restaurant/{restaurantId}")
	public boolean  approveByAdmin(@PathVariable long restaurantId ) {
		return restaurantDao.approveByAdmin(restaurantId);
	}
	
	
    // 관리자용 리뷰 전체 목록
    @GetMapping("/review/list")
    public List<ReviewAdminVO> reviewList() {
        return reviewDao.selectListAdmin();
    }

    // 관리자용 리뷰 삭제
    @DeleteMapping("/review/{reviewNo}")
    public boolean deleteReview(@PathVariable int reviewNo) {
        return reviewDao.delete(reviewNo);
    }

    // 배너 등록
    @PostMapping("/banner")
    public void addBanner(
            @ModelAttribute BannerDto bannerDto,
            @RequestParam(required = false) MultipartFile attach
    ) throws IOException {

        int bannerNo = bannerDao.sequence();
        bannerDto.setBannerNo(bannerNo);

        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach);
            bannerDto.setBannerAttachmentNo(attachmentNo);
        }

        bannerDao.insert(bannerDto);
    }

    // 배너 삭제 
    @DeleteMapping("/banner/{bannerNo}")
    public void deleteBanner(@PathVariable int bannerNo) {

        BannerDto bannerDto = bannerDao.selectOne(bannerNo);
        if (bannerDto == null) {
            throw new TargetNotFoundException("존재하지 않는 배너 번호");
        }

        bannerDao.delete(bannerNo);

        if (bannerDto.getBannerAttachmentNo() != null) {
            attachmentService.delete(bannerDto.getBannerAttachmentNo());
        }
    }

    // 카테고리 등록
    @PostMapping("/category")
    public void addCategory(@RequestBody CategoryDto categoryDto) {
        int seq = (int) categoryDao.sequence();
        categoryDto.setCategoryNo(seq);
        categoryDao.insert(categoryDto);
    }

    // 카테고리 수정
    @PutMapping("/category/{categoryNo}")
    public void editCategory(
            @PathVariable int categoryNo,
            @RequestBody CategoryDto categoryDto
    ) {
        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }
        categoryDto.setCategoryNo(categoryNo);
        categoryDao.updateUnit(categoryDto);
    }
    
    // 카테고리 부분 수정 
    @PatchMapping("/category/{categoryNo}")
    public void editCategoryUnit(
            @PathVariable int categoryNo,
            @RequestBody CategoryDto categoryDto
    ) {
        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }

        categoryDto.setCategoryNo(categoryNo);
        categoryDao.updateUnit(categoryDto);
    }

    // 카테고리 삭제
    @DeleteMapping("/category/{categoryNo}")
    public void deleteCategory(@PathVariable int categoryNo) {
        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }
        categoryDao.delete(categoryNo);
    }

    // 카테고리 이미지 등록 
    @PostMapping("/category/image")
    public void addCategoryImage(
            @RequestParam int categoryNo,
            @RequestParam MultipartFile attach
    ) throws IOException {

        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }

        Integer oldAttachmentNo = categoryDao.selectCategoryImageAttachmentNo(categoryNo);

        if (oldAttachmentNo != null) {
            attachmentService.delete(oldAttachmentNo);
        }

        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach);
            categoryDao.insertCategoryImage(categoryNo, attachmentNo);
        }
    }

    //카테고리 이미지 삭제
    @DeleteMapping("/category/image/{categoryNo}")
    public void deleteCategoryImage(@PathVariable int categoryNo) {

        if (categoryDao.selectOne(categoryNo) == null) {
            throw new TargetNotFoundException("존재하지 않는 카테고리");
        }

        Integer attachmentNo =
                categoryDao.selectCategoryImageAttachmentNo(categoryNo);

        if (attachmentNo != null) {
        	attachmentService.delete(attachmentNo);
        }
    }
    
    //지역 이미지 등록
    @PostMapping("/place/image")
    public void addPlaceImage(
            @RequestParam Long placeId,
            @RequestParam MultipartFile attach
    ) throws IOException {

        if (placeDao.selectOne(placeId) == null) {
            throw new TargetNotFoundException("존재하지 않는 지역");
        }

        Integer oldAttachmentNo = placeDao.selectPlaceImageAttachmentNo(placeId);

        if (oldAttachmentNo != null) {
            attachmentService.delete(oldAttachmentNo);
        }

        if (attach != null && !attach.isEmpty()) {
            int attachmentNo = attachmentService.save(attach); 
            placeDao.insertPlaceImage(placeId, attachmentNo);
        }
    }
    //지역 이미지 삭제
    @DeleteMapping("/place/image/{placeId}")
    public void deletePlaceImage(@PathVariable Long placeId) {

        if (placeDao.selectOne(placeId) == null) {
            throw new TargetNotFoundException("존재하지 않는 지역");
        }

        Integer attachmentNo = placeDao.selectPlaceImageAttachmentNo(placeId);

        if (attachmentNo != null) {
            attachmentService.delete(attachmentNo);
        }
    }
    
	//복합검색 (기능 테스트용 잘 됨) 그냥 dao에 만든거 관리자 컨트롤러로 옮길것
	@PostMapping("/member/search") //어쩔 수 없는 선택
	public List<MemberDto> search(@RequestBody MemberComplexSearchVO vo) {
		return sqlSession.selectList("member.complexSearch", vo);
	}
    
	//회원 탈퇴 기능 스케줄러로 설정해야됨 일단 비활성화 기능 만듬
	@PatchMapping("/{memberId}/deactivate")
	public boolean deactivate(@PathVariable String memberId) {
		MemberDto originDto = memberDao.selectOne(memberId);
		if(originDto == null) throw new TargetNotFoundException();
		if("관리자".equals(originDto.getMemberLevel())) {
			throw new UnauthorizationException();
		}
//		memberDto.setMemberId(memberId);
		memberDao.updateWithdraw(memberId);
		return true;
	}	
	
	//회원 탈퇴 롤백 기능 관리자만 허용
	@PatchMapping("/{memberId}/reactivate")
	public void reactivate(@PathVariable String memberId ) {
		MemberDto originDto = memberDao.selectOne(memberId);
		if(originDto == null) throw new TargetNotFoundException();
		if("관리자".equals(originDto.getMemberLevel())) {
			throw new UnauthorizationException();
		}
		memberDao.updateReactivate(memberId);
			
	}
	
	@GetMapping("/{memberId}")
	public MemberDto selectActiveOne(@PathVariable String memberId) {
		MemberDto memberDto = memberDao.selectActiveOne(memberId);
		if(memberDto == null) throw new TargetNotFoundException("존재하지 않는 회원");
		return memberDto;
	}
	
}

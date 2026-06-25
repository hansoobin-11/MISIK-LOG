package com.milky.misiklog.restcontroller;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.milky.misiklog.dao.MemberDao;
import com.milky.misiklog.dao.MemberTokenDao;
import com.milky.misiklog.dto.MemberDto;
import com.milky.misiklog.dto.RestaurantDto;
import com.milky.misiklog.error.TargetNotFoundException;
import com.milky.misiklog.error.UnauthorizationException;
import com.milky.misiklog.service.TokenService;
import com.milky.misiklog.vo.MemberComplexSearchVO;
import com.milky.misiklog.vo.MemberLoginResponseVO;
import com.milky.misiklog.vo.MemberNotAdminListVO;
import com.milky.misiklog.vo.MemberRefreshVO;
import com.milky.misiklog.vo.PageVO;
//import com.milky.misiklog.vo.RestaurantApprovalListVO;
import com.milky.misiklog.vo.TokenVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "회원 관리 컨트롤러")
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/member")
public class MemberRestController {

	@Autowired
    private TokenService tokenService;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MemberTokenDao memberTokenDao;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private SqlSession sqlSession;
	

    MemberRestController(TokenService tokenService) {
        this.tokenService = tokenService;
    }
	
	//차후 추가예정
//	@Autowired

	
	//회원가입
	//- @Valid가 붙음으로 인해 DTO에 설정된 Annotation이 일을 한다
	//-SpringDoc 설정을 추가하여 문서에 정보를 정확하게 기재한다
	@Operation(
			deprecated = false //비추천 여부 향후 사용 중지 예정이라면 true를 작성
			, description = "회원 가입을 위한 등록 기능" //기능에 대한 설명
			, responses = {//예상되는 응답 코드 
					@ApiResponse(responseCode = "200"), 
					@ApiResponse(responseCode = "400"),
					@ApiResponse(responseCode = "500")
			}
	)
	
	@PostMapping("/")
	public void add(@Valid @RequestBody MemberDto memberDto) {
		//등록 처리
		memberDao.insert(memberDto); 
	}
	
	//아이디 중복 검사
	// - 클라이언트에게 true(사용 가능) 또는 false(사용 중) 보내기
	//@GetMapping("/{memberId}") 이건 그냥 상세조회가 되어버림
	@GetMapping("/memberId/{memberId}") //커스텀 기능
	public boolean checkmemberId(@PathVariable String memberId) {
		MemberDto MemberDto  = memberDao.selectOne(memberId);
		return MemberDto == null;
	}
	
	//닉네임 중복검사
	@GetMapping("/memberNickname/{memberNickname}")
	public boolean checkmemberNickname(@PathVariable String memberNickname) {
		MemberDto memberDto = 
				memberDao.selectOneBymemberNickname(memberNickname);
				//xml mapper의 구문 ID는 detailBymemberNickname으로 합니다
				//이유는 비슷한 기능에 동일한 이름의 함수를 중복 사용 불가라서
		return memberDto == null;
	}
	
	//로그인
	@PostMapping("/login")
	public MemberLoginResponseVO login(@RequestBody MemberDto memberDto) {
		MemberDto findDto = memberDao.selectOne(memberDto.getMemberId());
		MemberDto findDormant = memberDao.findDormant(memberDto.getMemberId());
		if(findDto == null) { //아이디 없음
//			return false;
			throw new TargetNotFoundException("로그인 정보 오류");
		}
		if(findDormant.getMemberStatus().contains("DORMANT") ) {
			throw new UnauthorizationException("잠긴 계정입니다");
		}
		
		
		//암호화 떄문에 기존 코드처럼 dto에서 불러와 비교가 불가능
		boolean valid = passwordEncoder.
				matches(memberDto.getMemberPw(), findDto.getMemberPw());
		//주의사항으로는 순서가 반대면 안됨 앞이 전 뒤가 후
		if(valid == false) { //비번 불일치
			throw new TargetNotFoundException("로그인 정보 오류");

		}
		memberDao.updateLoginTime(memberDto.getMemberId());
		return MemberLoginResponseVO.builder()
					.loginId(findDto.getMemberId()) //아이디
					.loginLevel(findDto.getMemberLevel()) //등급
					.accessToken(tokenService.generateAccessToken(findDto)) //액세스 토큰
					.refreshToken(tokenService.generateRefreshToken(findDto)) //갱신 토큰
				.build();
	}
	
	@PostMapping("/refresh")
	public MemberLoginResponseVO refresh(
			@RequestBody MemberRefreshVO memberRefreshVO) {
		String refreshToken = memberRefreshVO.getRefreshToken();
		if(refreshToken == null) throw new UnauthorizationException();
		
		TokenVO tokenVO = tokenService.parse(refreshToken);
		//아이디와 토큰 문자열로 발급 내역을 조회하여 비교
		boolean valid = tokenService.checkRefreshToken(tokenVO, refreshToken);
		if(valid == false) throw new TargetNotFoundException();
		
		//재생성 후 반환
		return MemberLoginResponseVO.builder()
					.loginId(tokenVO.getLoginId())
					.loginLevel(tokenVO.getLoginLevel())
					.accessToken(tokenService.generateAccessToken(tokenVO))
					.refreshToken(tokenService.generateRefreshToken(tokenVO))
				.build();
	}
	
	
	//로그아웃
	@DeleteMapping("/logout")
	public void logout(
				@RequestHeader("Authorization") String bearerToken
	) {
		TokenVO tokenVO = tokenService.parse(bearerToken);
		memberTokenDao.deleteByTarget(tokenVO.getLoginId());
	}		
	
	
	//복합검색 (기능 테스트용 잘 됨) 그냥 dao에 만든거 관리자 컨트롤러로 옮길것
	@PostMapping("/search") //어쩔 수 없는 선택
	public List<MemberDto> search(@RequestBody MemberComplexSearchVO vo) {
		return sqlSession.selectList("member.complexSearch", vo);
	}
	
	
	//회원 정보 변경 (일례로 실제 회원 탈퇴는 여기서 플래그만 잡아둠
	@PatchMapping("/{memberId}")
	public void update(@PathVariable String memberId,
												@RequestBody MemberDto memberDto) {
		MemberDto originDto = memberDao.selectOne(memberId);
		if(originDto == null) throw new TargetNotFoundException();
		
		memberDto.setMemberId(memberId);
		memberDao.updateMember(memberDto);
		memberDao.updateChangeTime(memberId);
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
	
	
	//자영업자 삽입용 맵퍼
	@PostMapping("/business")
	public void addBusinessMember(@Valid @RequestBody MemberDto memberDto) {
		//등록 처리
		memberDao.insertOwner(memberDto); 
	}
	
	
	//관리자 삽입용 맵퍼 (삭제 필요?)
	@PostMapping("/admin")
	public void addAdmin(@Valid @RequestBody MemberDto memberDto) {
		//등록 처리
		memberDao.insertAdmin(memberDto); 
	}
	
	//스케줄링 해야됨 한번 나중에 한번 볼것
	
	//회원 탈퇴 롤백 기능 관리자만 허용
	@PatchMapping("/{memberId}/reactivate")
	public boolean reactivate(@PathVariable String memberId ) {
		MemberDto originDto = memberDao.selectOne(memberId);
		if(originDto == null) throw new TargetNotFoundException();
		if("관리자".equals(originDto.getMemberLevel())) {
			throw new UnauthorizationException();
		}
		memberDao.updateReactivate(memberId);
		return true;
	}
	
	@GetMapping("/{memberId}")
	public MemberDto selectActiveOne(@PathVariable String memberId) {
		MemberDto memberDto = memberDao.selectActiveOne(memberId);
		if(memberDto == null) throw new TargetNotFoundException("존재하지 않는 회원");
		return memberDto;
	}
//	@PostMapping("/{memberId}")
//	public MemberDto selectActiveOne(@PathVariable String memberId) {
//		MemberDto memberDto = memberDao.selectActiveOne(memberId);
//		if(memberDto == null) throw new TargetNotFoundException("존재하지 않는 회원");
//		return memberDto;
//	}
	
	@GetMapping("/findDormant")
	public List<MemberDto> selectDormant() {
		return sqlSession.selectList("member.findDormantMember");
	}
	
	
    // 검색 + 일반 페이징
    @GetMapping("/page/{page}")
    public MemberNotAdminListVO approvalList(@PathVariable int page) {
        PageVO pageVO = new PageVO();
        pageVO.setPage(page);
        pageVO.setDataCount(memberDao.notAdminCount());

        List<MemberDto> list = memberDao.notAdminList(pageVO);

        return MemberNotAdminListVO.builder()
                .page(pageVO.getPage())
                .size(pageVO.getSize())
                .count(pageVO.getDataCount())
                .begin(pageVO.getBegin())
                .end(pageVO.getEnd())
                .last(pageVO.getPage() >= pageVO.getTotalPage())
                .list(list)
                .build();
    }
	
	
}

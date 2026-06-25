package com.milky.misiklog.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.milky.misiklog.error.UnauthorizationException;
import com.milky.misiklog.service.TokenService;
import com.milky.misiklog.vo.TokenVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class MemberInterceptor implements HandlerInterceptor {
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//목표: 사용자가 보낸 요청의 헤더에 있는 Authorization 분석 및 판정
		//[1] OPTIONS라는 요청은 통과
		//- 통신이 가능한 대상인지 확인하는 선발대 형식의 통신
		//- CORS 상황이거나, 일반적인 요청이 아니면 발생 (Cross Origin Resource Sharing) 비동기 자원공유
		// 일반적(GET/POST/HEAD) 비 일반적(PUT/PATCH 등)
		if(request.getMethod().equalsIgnoreCase("options")) {
			return true;
		}
		
		//[2] 인증 검사
		try {
			String authorization = request.getHeader("Authorization");
			if(authorization == null) throw new UnauthorizationException();
			
			TokenVO tokenVO = tokenService.parse(authorization);
			
			request.setAttribute("tokenVO", tokenVO);
			System.out.println("로그인 상태입니다");
			return true;
		}
		catch(Exception e) {
			System.out.println("로그인 상태가 아니네요");
			e.printStackTrace();
			response.sendError(401);
			return false;
		}
		
	}
	
	
}

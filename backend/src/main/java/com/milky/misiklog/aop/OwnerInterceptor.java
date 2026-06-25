package com.milky.misiklog.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.milky.misiklog.error.NeedPermissionException;
import com.milky.misiklog.error.UnauthorizationException;
import com.milky.misiklog.service.TokenService;
import com.milky.misiklog.vo.TokenVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class OwnerInterceptor implements HandlerInterceptor{
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
		
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty()) {
			throw new UnauthorizationException("로그인이 필요합니다");
		}
		
		TokenVO tokenVO = tokenService.parse(token);
		
		if(tokenVO.getLoginId() == null) {
			throw new UnauthorizationException("로그인이 필요합니다");
		}
		
		if(!tokenVO.getLoginLevel().equals("자영업자")) {
			throw new NeedPermissionException("비즈회원만 접근 가능한 페이지입니다");
		}
		
		return true;
		}
}

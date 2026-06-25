package com.milky.misiklog.aop;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.milky.misiklog.error.NeedPermissionException;
import com.milky.misiklog.error.UnauthorizationException;
import com.milky.misiklog.service.TokenService;
import com.milky.misiklog.vo.TokenVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AdminInterceptor implements HandlerInterceptor{

    private final TokenService tokenService;

    public AdminInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {


        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new UnauthorizationException("로그인이 필요합니다");
        }
        
        TokenVO tokenVO = tokenService.parse(token);

        if (tokenVO.getLoginId() == null) {
            throw new UnauthorizationException("로그인이 필요합니다");
        }

        if (!"관리자".equals(tokenVO.getLoginLevel())) {
            throw new NeedPermissionException("관리자 권한이 없습니다");
        }

        return true;
    }
}
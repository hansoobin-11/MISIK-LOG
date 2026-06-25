package com.milky.misiklog.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
	@Autowired
	private TokenRenewalInterceptor tokenRenewalInterceptor;
	@Autowired
	private MemberInterceptor memberInterceptor;
	@Autowired
	private AdminInterceptor adminInterceptor;
	@Autowired
	private OwnerInterceptor ownerInterceptor;
	
	//현재 토큰 기능은 구현하지 않았습니다
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		 
		//로그인 이 필요한 기능은 아래 add에 추가하고 
		//제외 패턴은 exclude에 추가
		registry.addInterceptor(memberInterceptor)
							.addPathPatterns(
									"/admin/**",
									"/business/**",
									"/reservation/pay", 
									"/reservation/myList",
									"/reservation/refund",
									"/reservation/status/**",
									"/reservation/ownerList",
									"/payment/**",
									"/owner/**"
							)
							.excludePathPatterns(
									"/member/business"									
							);
		
		registry.addInterceptor(tokenRenewalInterceptor)
							.addPathPatterns("/**")
							.excludePathPatterns(
									"/member/refresh", //이미 만료되서 갱신하므로 갱신대상 아님
									"/member/join", //비회원 페이지들은 갱신대상아님
									"/member/login", //비회원 페이지들은 갱신대상아님
									"/member/logout", //토큰을 만료시켜야 하므로 갱신대상 아님
									"/cert/**" //인증은 비회원이 사용하는 기능
							);
		
		registry.addInterceptor(adminInterceptor)
        .addPathPatterns("/admin/**")
        .excludePathPatterns(
            "/error/**"
        );
		
		registry.addInterceptor(ownerInterceptor)
		.addPathPatterns("/restaurant/", "/owner/**")
		;
	}
	
}

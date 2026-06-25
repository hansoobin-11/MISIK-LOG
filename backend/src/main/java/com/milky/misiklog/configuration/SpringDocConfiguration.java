package com.milky.misiklog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocConfiguration {
	//정보 객체를 등록
	@Bean
	public OpenAPI info() {
		//문서의 대표 정보 생성
		Info info = new Info();
		//앞에 0.0.1이러면 서비스 전임
		//앞이 1.0 이러면 배포한거임(서비스 했다고)
		//마켓에서는 동일버전은 업그레이드 안됨 무조건 올려야됨
		info.setVersion("0.0.1");
		info.setTitle("KH 정보교육원 수업용 REST API");
		info.setDescription("RestJS와 통신하기 위한 모든 REST service 정보 명세");
		
		//추가 JWT 기반의 인증방식이 적용되었으므로 문서에 이를 반영해야 한다
		String jwtHeaderName = "Authorization"; //헤더이름
		SecurityRequirement requirement = new SecurityRequirement(); //보안 요구사항 객체
		requirement.addList(jwtHeaderName); //헤더 등록
		
		//추가 임시로그인 처리도구
		Components components = new Components();
		components.addSecuritySchemes(
				jwtHeaderName, 
				new SecurityScheme()
					//헤더 이름
					.name(jwtHeaderName)
					//통신 유형
					.type(SecurityScheme.Type.HTTP) 
					//토큰 종류
					.scheme("bearer") 
		);
		
		//api에  내가 설정한 정보를 추가하는거
		return new OpenAPI().info(info)
				.addSecurityItem(requirement)
				.components(components);
		
	}
}

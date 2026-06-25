package com.milky.misiklog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class ParameterConfiguration {

	//Spring Boot에서 가져가서 사용할 JSON 해석 도구를 생성
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();

		//mapper에 내가 만든 클래스를 규칙 옵션으로 설정
		SimpleModule module = new SimpleModule();
		module.addDeserializer(String.class, new EmptyStringToNullDeserializer());
		mapper.registerModule(module);
		
		mapper.registerModule(new JavaTimeModule());
		
		return mapper; 
	}
	
}

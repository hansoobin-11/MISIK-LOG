package com.milky.misiklog.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class EncryptConfiguration {
	//상속이 없고 가벼운 클래스를 지향
	@Bean
	public BCryptPasswordEncoder encoder() {
		//옵션 설정
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder;
	}
}

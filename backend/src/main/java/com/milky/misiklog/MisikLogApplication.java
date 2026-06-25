package com.milky.misiklog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

//스케쥴러는 서버에 부담이 많이가는 작업이므로 반드시 허용 설정을 하도록 요구
//아래 설정이 있어야 스프링 암호화 설정을 무시하고 진행 가능함
//@EnableScheduling
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MisikLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MisikLogApplication.class, args);
	}

}

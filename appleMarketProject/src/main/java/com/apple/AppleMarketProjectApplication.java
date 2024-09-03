package com.apple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

//exclude -> 스프링시큐리티 기본로그인 페이지 안나오게
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
//비동기처리를 위함
@EnableAsync
public class AppleMarketProjectApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AppleMarketProjectApplication.class, args);
	}

}
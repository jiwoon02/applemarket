package com.apple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//exclude -> 스프링시큐리티 기본로그인 페이지 안나오게
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AppleMarketProjectApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AppleMarketProjectApplication.class, args);
	}

}
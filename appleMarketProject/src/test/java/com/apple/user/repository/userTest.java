package com.apple.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apple.user.domain.User;
import com.apple.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class userTest {
	@Autowired
	UserService userService;
	
	@Test
	public void createUser() {
		User user = new User();
		user.setUserID("test");
		user.setUserPwd("test");
		user.setUserName("test");
		user.setUserPhone("010-1111-1111");
		user.setUserEmail("test@gmail.com");
		user.setUserBirth("2000-10-11");
		user.setUserNickname("test");
		user.setUserZonecode("12345");
		user.setUserAddress("서울시 00길 11");
		user.setUserAddressDetail("3층");
		
		userService.createUser(user);
	}
}

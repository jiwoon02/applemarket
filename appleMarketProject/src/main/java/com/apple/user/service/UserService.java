package com.apple.user.service;

import com.apple.user.domain.User;

public interface UserService{

	public void createUser(User user);

	boolean isUserIDAvailable(String userID);

	boolean isUserEmailAvailable(String userEmail);

	boolean isUserPhoneAvailable(String userPhone);

	boolean isUserNicknameAvailable(String userNickname);

	public String findId(User user);

	String findPwd(User user);
	
}

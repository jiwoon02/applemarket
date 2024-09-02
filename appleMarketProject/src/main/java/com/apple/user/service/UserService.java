package com.apple.user.service;

import java.util.Optional;


import com.apple.user.domain.User;

public interface UserService{

	public void createUser(User user);

	boolean isUserIDAvailable(String userID);

	boolean isUserEmailAvailable(String userEmail);

	boolean isUserPhoneAvailable(String userPhone);

	boolean isUserNicknameAvailable(String userNickname);

	public String findId(User user);

	public String findPwd(User user);

//	public Optional<User> findByUserID(String userID);
//
//	boolean authenticate(String userID, String password);
	
}

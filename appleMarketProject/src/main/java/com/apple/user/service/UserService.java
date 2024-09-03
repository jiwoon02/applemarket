package com.apple.user.service;

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

	//userNo로 이름을 얻어옴
	public String getNameByUserNo(long userNo);
	//userNo로 전화번호를 얻어옴
	public String getPhoneByUserNo(long userNo);
	//토큰으로 userNo를 얻어옴
	public Long getUserNo(String token);
}

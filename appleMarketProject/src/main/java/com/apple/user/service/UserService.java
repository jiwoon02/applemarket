package com.apple.user.service;

import com.apple.user.domain.User;

public interface UserService{

	public User createUser(User user);

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

	public void userLocationUpdate(Long userID, Long locationID);

	
	//토큰으로 userNo를 얻어옴
	public Long getUserNo(String token);
	
	// User의 LocationID를 가져오는 메서드
	public Long getLocationIDByUserNo(Long userNo);

}

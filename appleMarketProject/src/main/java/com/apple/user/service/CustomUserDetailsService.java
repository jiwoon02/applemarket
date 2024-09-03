package com.apple.user.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.apple.user.domain.User;
import com.apple.user.dto.CustomUserDetails;
import com.apple.user.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	//스프링시큐리티가 사용자 인등을 위해 사용자 정보를 로드할때 호출
	@Override
	public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
		//userID에 해당하는 사용자 조회
		Optional<User> userData = userRepository.findByUserID(userID);
		
		//사용자가 존재하면 customUserDetails 객체를 생성해서 반환
		if (userData.isPresent()) {
			User user = userData.get();
			//CustomUserDetails - 사용자 세부정보 객체
			return new CustomUserDetails(user);
        }
		 // 사용자 정보를 찾지 못한 경우 예외를 발생시킴
        throw new UsernameNotFoundException("User not found with userID: " + userID);
	}
	
}

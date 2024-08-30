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
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userData = userRepository.findByUserID(username);
		
		if (userData.isPresent()) {
			User user = userData.get();
			return new CustomUserDetails(user);
        }
		return null;
	}
	
}

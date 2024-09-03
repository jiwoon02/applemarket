package com.apple.main.controller;

import com.apple.jwt.JwtUtil;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class MainController {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	@GetMapping("/")
	public String main(HttpServletRequest request, Model model) {
		boolean isLoggedIn = false;
		
		//jwt 쿠키 확인
		return "client/main";
	}

	@ModelAttribute
	public void userDetailsToModel(@CookieValue(value = "JWT", required = false) String token, Model model) {
		if(token != null) {
		String userID = jwtUtil.getUserID(token);
			if(userID != null) {
				Optional<User> optionalUser = userRepository.findByUserID(userID);
				if (optionalUser.isPresent()) {
					User user = optionalUser.get();
					model.addAttribute("currentUserID", userID);
					model.addAttribute("userLocation", user.getLocation() != null ? user.getLocation().getLocationName() : null);
				} else {
					throw new IllegalArgumentException("해당 토큰의 유저를 찾을 수 없습니다.");
				}
			}
		}




	}


	


	
	
}

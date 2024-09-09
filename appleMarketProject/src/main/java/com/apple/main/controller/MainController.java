package com.apple.main.controller;


import com.apple.jwt.JwtUtil;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@RequiredArgsConstructor
@ControllerAdvice
public class MainController {

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;
	/*
        @GetMapping("/")
        public String main(HttpServletRequest request, Model model) {
            //boolean isLoggedIn = false;

            //jwt 쿠키 확인
            return "client/main";
        }
    */
	@ModelAttribute
	public void userDetailsToModel(@CookieValue(value = "JWT", required = false) String token, Model model) {
		String userID = null;
		User user = null;

		if (token != null) {
			userID = jwtUtil.getUserID(token);
			if (userID != null) {
				user = userRepository.findByUserID(userID).orElse(null);
			}
		}

		model.addAttribute("currentUserID", userID);
		model.addAttribute("userLocation", user != null ? user.getLocation() : null);
	}


}
package com.apple.main.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@GetMapping("/")
	public String main() {
		return "client/main";
	}
	
//	@GetMapping("/")
//	public String mainP() {
//		String userID = SecurityContextHolder.getContext().getAuthentication().getName();
//		
//		return "/client/main";
//	}
	
//	@GetMapping("/apple")
//	public String apple() {
//		return "client/apple";
//	}
	
	@GetMapping("/locationForm")
	public String locationForm() {
		return "location/locationForm";
	}
	
	
}

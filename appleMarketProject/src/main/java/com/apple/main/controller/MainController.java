package com.apple.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	/*
	@GetMapping("/")
	public String hello(Model model) {
		model.addAttribute("name", "홍길동");
		return "main";
		//main.html 이라는 파일을 찾는다는 의미
	}
	*/
	@GetMapping("/")
	public String main() {
		return "client/main";
	}
	
	@GetMapping("/apple")
	public String apple() {
		return "client/apple";
	}
	
	@GetMapping("/locationForm")
	public String locationForm() {
		return "location/locationForm";
	}
	
	
}

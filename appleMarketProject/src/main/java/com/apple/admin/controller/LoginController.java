package com.apple.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apple.admin.domain.Admin;
import com.apple.admin.service.AdminService;

import lombok.Setter;

@Controller
@RequestMapping("/admin/*")
public class LoginController {
	
	@Setter(onMethod_ = @Autowired)
	private AdminService adminService;
	
	@GetMapping("login")
	public String login(Admin admin, Model model) {
		List<Admin> adminList = adminService.adminList(admin);
		model.addAttribute("adminList", adminList);
		
		return "/admin/login";
	}
	
//	@PostMapping("login")
//	public String loginSecurity(@RequestParam String adminName, @RequestParam String adminPasswd, Model model)  {
//		boolean isAuthenticated = adminService.validateAdmin(adminName, adminPasswd);
//		if (isAuthenticated) {
//			return "redirect:/client/admin/login";
//		}else {
//			model.addAttribute("error", "로그인 실패");
//			return "/client/admin/login";
//		}
//	}
	
	


}

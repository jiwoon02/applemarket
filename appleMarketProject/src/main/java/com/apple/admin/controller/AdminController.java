package com.apple.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apple.admin.service.AdminService;
import com.apple.category.domain.Category;
import com.apple.product.domain.Product;
import com.apple.user.domain.User;

import lombok.Setter;


@Controller
@RequestMapping("/admin/success/*")
public class AdminController {
	
	@Setter(onMethod_ = @Autowired)
	private AdminService adminService;

	//카테고리 관리 페이지로 이동을 위한 매핑
	@GetMapping("category")
	public String categoryList(Category category, Model model) {
		List<Category> list = adminService.categoryList(category);
		model.addAttribute("categoryList", list);
		
		return "/admin/category";
	}
	//사용자 관리 페이지로 이동을 위한 매핑
	@GetMapping("administration")
	public String administration(User user, Model model) {
		List<User> list = adminService.userList(user);
		model.addAttribute("userList", list);
		
		return "/admin/administration";
	}
	//커뮤니티 관리 페이지로 이동을 위한 매핑
	@GetMapping("community")
	public String community() {
		
		return "/admin/community";
	}
	//상품 관리 페이지로 이동을 위한 매핑
	@GetMapping("product")
	public String product(Product product, Model model) {
		List<Product> list = adminService.productList(product);
		model.addAttribute("productList", list);
		
		return "/admin/product";
	}
	
	
	@PostMapping("category/insert")
	public String categoryInsert(Category category) {
		adminService.categoryInsert(category);
		return "redirect:/admin/success/category";
	}
	
	@PostMapping("category/delete")
	public String categoryDelete(Category category) {
		adminService.categoryDelete(category);
		return "redirect:/admin/success/category";
	}
	
	@PostMapping("administration/update")
	public String administrationUpdate(@ModelAttribute User user) {
		
		adminService.userUpdate(user);
		return "redirect:/admin/success/administration";
	}
	
	
	@GetMapping("/product/{productID}")
	public String proudctDetail(@PathVariable String productID, Model modle) {
	
		return "/client/admin/productDetail";
	}
//	테스트용 매핑 
	@GetMapping("product/1")
	public String proudctDetail(Model modle) {
	
		return "/admin/productDetail";
	}
}

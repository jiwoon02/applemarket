package com.apple.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.apple.product.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apple.admin.domain.ProductReport;
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
		Map<String, Long> categoryCount = adminService.CategoryCounts();
		
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
	public String product(Product product,ProductReport productReport, Model model) {
		List<Product> list = adminService.productList(product);
		//디버그
//		System.out.println("list : " + list);
		
		
		Map<Long, Long> reportCount = adminService.productReportCount();
		//디버그
		System.out.println("id : " + reportCount.values());
		model.addAttribute("reportCount", reportCount);
		model.addAttribute("productList", list);

		return "/admin/product";
	}
	
	@GetMapping("/product/{productID}")
	public String proudctDetail(@PathVariable Long productID,Product product,ProductReport productReport,Category category, Model model) {
		product.setProductID(productID);
		Product detail = adminService.productDetail(product);
		List<ProductReport> Reportdetail = adminService.productReportDetail(productReport);
		List<Category> categoryList = adminService.categoryList(category);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("detail", detail);
		model.addAttribute("reportDetail", Reportdetail);
		return "/admin/productDetail";
	}
	
	@PostMapping("product/{productID}/category/change")
	public String categoryChange(@PathVariable Long productID, @RequestBody Map<String, String> requestBody) {
	    String categoryID = requestBody.get("categoryID");
	    adminService.categoryChange(productID, categoryID);
	    return "redirect:/admin/success/product/" + productID;
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
	

	@PostMapping("product/{productID}/product/delete")
	public String productDelete(@PathVariable Long productID, @RequestBody List<Long> productIds ) {
		adminService.productDelete(productID, productIds);
		
		return "redirect:/admin/success/product";
	}
	
	@GetMapping("/product/{productID}")
	public String proudctDetail(@PathVariable Long productID,Product product,ProductReport productReport, Model model) {
		product.setProductID(productID);
		Product detail = adminService.productDetail(product);
		List<ProductReport> Reportdetail = adminService.productReportDetail();
		model.addAttribute("detail", detail);
		model.addAttribute("reportDetail", Reportdetail);
		return "/admin/productDetail";
	}

    @Autowired
    public void setProductServiceImpl(ProductServiceImpl productServiceImpl) {
		this.productServiceImpl = productServiceImpl;
	}
//	테스트용 매핑 
//	@GetMapping("product/1")
//	public String proudctDetail(Model modle) {
//	
//		return "/admin/productDetail";
//	}
}

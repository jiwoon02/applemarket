package com.apple.admin.controller;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.apple.product.service.ProductService;
import com.apple.product.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import com.apple.admin.domain.CommunityReport;
import com.apple.admin.domain.ProductReport;
import com.apple.admin.service.AdminService;
import com.apple.category.domain.Category;
import com.apple.client.community.domain.CommunityPost;
import com.apple.client.community.repository.CommunityPostRepository;
import com.apple.product.domain.Product;
import com.apple.user.domain.User;

import lombok.Setter;


@Controller
@RequestMapping("/admin/success/*")
public class AdminController {
	
	@Setter(onMethod_ = @Autowired)
	private AdminService adminService;
	
	@Setter(onMethod_ = @Autowired)
	private CommunityPostRepository communityPostRepository;
	
	private ProductServiceImpl productServiceImpl;

	//카테고리 관리 페이지로 이동을 위한 매핑
	@GetMapping("category")
	public String categoryList(Category category, Model model) {
		List<Category> list = adminService.categoryList(category);
		Map<String, Long> categoryCount = adminService.CategoryCounts();
		
		model.addAttribute("categoryList", list);
        model.addAttribute("Count", categoryCount);
		
		return "/admin/category";
	}
	//사용자 관리 페이지로 이동을 위한 매핑
	@GetMapping("administration")
	public String administration(User user, Model model) {
		List<User> list = adminService.userList(user);
		model.addAttribute("userList", list);
		
		return "/admin/administration";
	}
//	커뮤니티 관리 페이지로 이동을 위한 매핑
	@GetMapping("community")
	public String community(CommunityPost communityPost, Model model) {
		List<CommunityPost> list = adminService.communityPostList(communityPost);
		Map<Long, Long> reportCount = adminService.communityReportCount();
		//디버그
		System.out.println("id :" + reportCount.toString());
		System.out.println("values : " + reportCount.values());
		model.addAttribute("reportCount", reportCount);
		model.addAttribute("posts", list);
		return "/admin/community";
	}
	
	@GetMapping("/community/detail/{communityPostID}")
	public String community(@PathVariable Long communityPostID, CommunityReport communityReport,CommunityPost communityPost, Model model) {
		communityPost.setCommunityPostID(communityPostID);
		CommunityPost detail = adminService.communityPostDetail(communityPost);
		List<CommunityReport> Reportdetail = adminService.communityReportDetail(communityReport);
		model.addAttribute("communityPost", detail);
		model.addAttribute("reportDetail", Reportdetail);
		
		return "/admin/communityDetail";
	}
	//상품 관리 페이지로 이동을 위한 매핑
	@GetMapping("product")
	public String product(Product product,ProductReport productReport, Model model) {
		List<Product> list = adminService.productList(product);
		//디버그
//		System.out.println("list : " + list);
        Map<Long, Long> reportCount = adminService.productReportCount();
		//디버그
        System.out.println("id :" + reportCount.toString());
		System.out.println("values : " + reportCount.values());
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
	
	@PostMapping("/community/{communityPostID}/detail/delete")
	public String communityDelete(@PathVariable Long communityPostID, @RequestBody List<Long> communityPostids) {
		adminService.commnuityDelete(communityPostID, communityPostids);
		
		return "redirect:/admin/success/community";
	}
	
    @Autowired
    public void setProductServiceImpl(ProductServiceImpl productServiceImpl) {
		this.productServiceImpl = productServiceImpl;
	}
    
    @GetMapping("/image/base64/{postId}")
    public ResponseEntity<String> getBase64Image(@PathVariable Long postId) {
        Optional<CommunityPost> postOptional = communityPostRepository.findById(postId);

        if (postOptional.isPresent()) {
            byte[] imageBytes = postOptional.get().getCommunityImage();

            if (imageBytes != null && imageBytes.length > 0) {
                String mimeType = "image/jpeg";  // 기본적으로 JPEG 형식으로 가정

                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                String imageDataUrl = "data:" + mimeType + ";base64," + base64Image;

                return ResponseEntity.ok(imageDataUrl);
            } else {
                return ResponseEntity.ok(null);  // 이미지가 없을 때 null 반환
            }
        }

        return ResponseEntity.notFound().build();
    }
//	테스트용 매핑 
//	@GetMapping("product/1")
//	public String proudctDetail(Model modle) {
//	
//		return "/admin/productDetail";
//	}
}

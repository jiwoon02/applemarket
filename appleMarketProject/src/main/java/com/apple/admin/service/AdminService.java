package com.apple.admin.service;

import java.util.List;
import java.util.Map;

//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.apple.admin.domain.Admin;
import com.apple.admin.domain.ProductReport;
import com.apple.category.domain.Category;
import com.apple.client.community.domain.CommunityPost;
import com.apple.product.domain.Product;
import com.apple.user.domain.User;
//import com.apple.admin.domain.Category;
//import com.apple.admin.domain.Product;
//import com.apple.admin.domain.LoginRequest;
//import com.apple.admin.domain.User;

public interface AdminService {
	
	public List<Admin> adminList(Admin admin);
	public List<Category> categoryList(Category category);
	public List<User> userList(User user);
	public List<Product> productList(Product product);
	public List<CommunityPost> communityPostList(CommunityPost communityPost);
	public List<ProductReport> productReportDetail(ProductReport product);
	public List<ProductReport> productReportDetail();
	public Map<Long, Long> productReportCount();
	public Map<String, Long> CategoryCounts();
	public void userUpdate(User user);
	public void categoryInsert(Category category);
	public void categoryDelete(Category category);
	public void productDelete(Long productID , List<Long> productIds);
	public void commnuityDelete(Long commuityPostID , List<Long> communityPostids);
	public void categoryChange(Long productID, String newCategoryID); 
	public Product productDetail(Product product);
	public CommunityPost communityPostDetail(CommunityPost communityPost);
	//public List<ProductReport> productReportDetail(ProductReport product);
//	public boolean validateAdmin(String adminName, String adminpasswd);
//	public UserDetails loadAdminByAdminName(String AdminName) throws UsernameNotFoundException;
	
}


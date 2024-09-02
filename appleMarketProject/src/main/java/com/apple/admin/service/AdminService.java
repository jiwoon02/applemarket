package com.apple.admin.service;

import java.util.List;

//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.apple.admin.domain.Admin;
import com.apple.category.domain.Category;
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
	public void userUpdate(User user);
	public void categoryInsert(Category category);
	public void categoryDelete(Category category);
//	public boolean validateAdmin(String adminName, String adminpasswd);
//	public UserDetails loadAdminByAdminName(String AdminName) throws UsernameNotFoundException;
	
}

package com.apple.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.apple.admin.domain.Admin;
import com.apple.admin.domain.ProductReport;
import com.apple.admin.repository.AdminRepository;
import com.apple.admin.repository.CategoryRepository;
import com.apple.admin.repository.ProductReportRepository;
import com.apple.category.domain.Category;
import com.apple.product.domain.Product;
import com.apple.product.repository.ProductRepository;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;

import lombok.Setter;

@Service
public class AdminServiceImpl implements AdminService {

	@Setter (onMethod_ = @Autowired)
	private AdminRepository adminRepository;
//	
	@Setter (onMethod_ = @Autowired)
	private CategoryRepository categoryRepository;
//	
	@Setter (onMethod_ = @Autowired)
	private UserRepository userRepository;
	
	@Setter (onMethod_ = @Autowired)
	private ProductRepository productRepository;
	
	@Setter (onMethod_ = @Autowired)
	private ProductReportRepository productReportRepository;
	
//	@Setter (onMethod_ = @Autowired)
//	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public List<Admin> adminList(Admin admin) {
		List<Admin> adminList = null;
		adminList = (List<Admin>) adminRepository.findAll();
		return adminList;
	}

	@Override
	public List<Category> categoryList(Category category) {
		List<Category> categoryList = null;
		categoryList = (List<Category>) categoryRepository.findAll();
		return categoryList;
	}

	@Override
	public List<User> userList(User user) {
		List<User> userList = null;
		userList = (List<User>) userRepository.findAll();
		
		return userList;
	}
	
	@Override
	public List<Product> productList(Product product) {
		List<Product> productList = null;
		productList = (List<Product>) productRepository.findAll();
		return productList;
	}

	@Override
	public Map<Long, Long> productReportCount(ProductReport productReport) {
		Map<Long, Long> productReportCount = null;
		productReportCount = (Map<Long, Long>) productReportRepository.ReportConut();
		return productReportCount;
	}
	
	@Override
	public void categoryInsert(Category category) {
		categoryRepository.save(category);
	}

	@Override
	public void categoryDelete(Category category) {
		categoryRepository.deleteById(category.getCategoryID());
		
	}

	@Override
	public void userUpdate(User user) {
		Optional<User> UserOptional = userRepository.findById(user.getUserNo());
		User updateUser = UserOptional.get();
		updateUser.getUserID();
		updateUser.setUserName(user.getUserName());
		userRepository.save(updateUser);
	}

	@Override
	public Product productDetail(Product product) {
		Optional<Product> productOptional = productRepository.findById(product.getProductID());
		Product detail = productOptional.get();
		return detail;
	}

	@Override
	public List<ProductReport> productReportDetail(ProductReport productReport) {
		 List <ProductReport> ProductReportList = null;
		ProductReportList = (List<ProductReport>) productReportRepository.findByProductID(productReport.getProductID());
		return ProductReportList;
	}



//	@Override
//	public boolean validateAdmin(String adminName, String adminPasswd) {
//		Admin admin = adminRepository.findByAdminName(adminName);
//		if (admin == null) {
//			throw new UsernameNotFoundException("user not found");
//		} 
//		if (admin != null) {
//			return passwordEncoder.matches(adminPasswd, admin.getAdminPasswd());
//			
//		}
//		return false;
//	}
//	
//	public void registerUser(String adminName, String adminPasswd) {
//		String encodedPassword = passwordEncoder.encode(adminPasswd);
//		Admin newAdmin = new Admin(adminName, encodedPassword);
//		adminRepository.save(newAdmin);
//	}

//	@Override
//	public UserDetails loadAdminByAdminName(String AdminName) throws UsernameNotFoundException {
//		Admin admin = adminRepository.findByAdminName(AdminName);
//				
//		return org.springframework.security.core.userdetails.User
//		        .withUsername(admin.getAdminName())
//		        .password(admin.getAdminPasswd())
//		        .roles("ADMIN")
//		        .build();
//	}

	
	
	
	
	
	

	

	
}

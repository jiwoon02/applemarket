package com.apple.admin.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.apple.product.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private ProductServiceImpl productServiceImpl;

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
	public void categoryChange(Long productID, String newCategoryID) {
		Optional<Product> productOptional = productRepository.findById(productID);
		if (productOptional.isPresent()) {
			Product existingProduct = productOptional.get();
			
			// Find the new category
			Optional<Category> categoryOptional = categoryRepository.findById(newCategoryID);
			if (categoryOptional.isPresent()) {
				Category newCategory = categoryOptional.get();
				existingProduct.setCategory(newCategory);
				productRepository.save(existingProduct);
			} else {
				throw new IllegalArgumentException("Category not found");
			}
		} else {
			throw new IllegalArgumentException("Product not found");
		}
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
/*
	@Override
	public List<ProductReport> productReportDetail(ProductReport productReport) {
		 List <ProductReport> ProductReportList = null;
		 ProductReportList = (List<ProductReport>) productReportRepository.findAll();
		//ProductReportList = (List<ProductReport>) productReportRepository.findByProduct();
		return ProductReportList;
	}
*/
	@Override
	public Map<Long, Long> productReportCount() {
		List<Object[]> results = productReportRepository.ReportCount();
	
		Map<Long, Long> productReportCount = new HashMap<>();
		for (Object[] result : results) {
			Long productId = ((Number) result[0]).longValue();
			Long count = ((Number) result[1]).longValue();
			productReportCount.put(productId, count);
		}
		
		return productReportCount;
	}
	//카테고리 등록 횟수 표시
	public Map<String, Long> CategoryCounts() {
        List<Object[]> results = productRepository.countProductsByCategory();

        Map<String, Long> categoryCounts = new HashMap<>();
        for (Object[] result : results) {
            String categoryId = (String) result[0];
            Long count = ((Number) result[1]).longValue();
            categoryCounts.put(categoryId, count);
        }

        return categoryCounts;
    }

	@Transactional
	@Override
	public void productDelete(Long productID , List<Long> productIds) {
		//신고된 내용 삭제
		productReportRepository.deleteByProductIds(productIds);
		productRepository.deleteById(productID);
		
	}




	public List<ProductReport> productReportDetail(){
		List<ProductReport> productReportList = productReportRepository.findAll();

		for(ProductReport productReport : productReportList){
			Long productID = productReport.getProduct().getProductID();
			Long reportCount = productServiceImpl.getReportCountByProductID(productID);
		}

		return productReportList;
}
    @Autowired
    public void setProductServiceImpl(ProductServiceImpl productServiceImpl) {
		this.productServiceImpl = productServiceImpl;
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

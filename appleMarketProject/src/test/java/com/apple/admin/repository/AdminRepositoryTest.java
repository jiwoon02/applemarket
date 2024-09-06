package com.apple.admin.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apple.admin.domain.Admin;
import com.apple.admin.domain.ProductReport;
//import com.apple.admin.domain.Product;
//import com.apple.admin.domain.User;
import com.apple.product.domain.Product;
import com.apple.product.repository.ProductRepository;
import com.apple.user.domain.User;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class AdminRepositoryTest {
	@Setter(onMethod_ = @Autowired )
	private AdminRepository adminRepository;
	
//	@Setter(onMethod_ = @Autowired)
//	private UserRepository userRepository;
//	
	@Setter(onMethod_ = @Autowired)
	private ProductRepository productRepository;

	@Setter(onMethod_ = @Autowired)
	private ProductReportRepository productReportRepository;
//	@Test
//	public void adminInsertTest() {
//		Admin admin = new Admin();
//		admin.setAdminName("admin");
//		admin.setAdminPasswd("1234");
//		log.info("### admin 테이블에 첫번째 데이터 입력");
//		adminRepository.save(admin);
//		
//	}
//	@Test
//	public void productInsertTest() {
//		Product product = new Product();
//		product.setProductID(21454L);
//		product.setProductPrice(100000);
//		product.setPostPrice(2500);
//		product.setProductName("상품 이름");
////		product.setProductImages("상품 이미지");
//		product.setProductDescription("상품 상세 설명");
//		productRepository.save(product);
//		
//	}
//
//	@Test
//	public void productListTest() {
//		List<Product> productLst = (List<Product>) productRepository.findAll();
//		for(Product product : productLst) {
//			log.info(product.toString());
//			log.info(product.getProductName());
//		}
//	}
//	
	//신고내용 입력
	/*
	@Test
	public void productReprotdtailInsert() {
		ProductReport productReport = new ProductReport();
		Product product = new Product();
		User user = new User();
		
		user.setUserNo(1L);
		product.setProductID(11L);
		productReport.setUser(user);
		productReport.setProductID(product);
		productReport.setReportContent("대충 신고하는 내용 중복");
		productReportRepository.save(productReport);
	}
	*/

//	@Test
//	public void adminListTest() {
//		List<Admin> adminList = (List<Admin>) adminRepository.findAll();
//		for(Admin admin: adminList) {
//			log.info(admin.toString());
//		}
//	}
	
//	@Test
//	public void userInsertTest() {
//		User user = new User();
//		user.setUserID("ghasd");
//		user.setUserPhone("010-1111-2222");
//		user.setUserPwd("1234");
//		user.setUserName("maiami");
//		user.setUserNickname("사용자");
//		user.setUserZonecode("우편주");
//		user.setUserAddress("1층");
//		user.setUserAddressDetail("언주로164길 39");
//		user.setUserEmail("ghasd@naver.com");
//		log.info("### User 테이블에 첫번째 데이터 입력");
//		userRepository.save(user);
//	}
//	
	

}

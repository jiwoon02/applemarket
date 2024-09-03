package com.apple.product.repository;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apple.product.domain.Product;
import com.apple.product.domain.ProductImages;
import com.apple.product.service.ProductService;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ProductTests {
	
	@Setter(onMethod_ = @Autowired)
	ProductImagesRepository productImagesRepository;
	
	@Test
	public void insertProductImages() {
		Product product = new Product();
		product.setProductID(24L);
		ProductImages images = new ProductImages();
		images.setFilename("https://cdn.pixabay.com/photo/2015/06/25/17/21/smart-watch-821557_1280.jpg");
		productImagesRepository.save(images);
		log.info("ProductImages 테이블에 테이터 1 입력");
		
		Product product1 = new Product();
		product1.setProductID(21L);
		ProductImages images1 = new ProductImages();
		images1.setFilename("https://cdn.pixabay.com/photo/2020/04/10/01/17/airpods-5023660_1280.jpg");
		productImagesRepository.save(images1);
		log.info("ProductImages 테이블에 테이터 2 입력");
		
		Product product2 = new Product();
		product2.setProductID(22L);
		ProductImages images2 = new ProductImages();
		images2.setFilename("https://cdn.pixabay.com/photo/2021/02/06/07/02/laptop-5987093_1280.jpg");
		productImagesRepository.save(images2);
		log.info("ProductImages 테이블에 테이터 3 입력");

		Product product3 = new Product();
		product3.setProductID(23L);
		ProductImages images3 = new ProductImages();
		images3.setFilename("https://cdn.pixabay.com/photo/2016/03/27/07/12/apple-1282241_1280.jpg");
		productImagesRepository.save(images3);
		log.info("ProductImages 테이블에 테이터 4 입력");
		
		Product product4 = new Product();
		product4.setProductID(30L);
		ProductImages images4 = new ProductImages();
		images4.setFilename("https://cdn.pixabay.com/photo/2015/01/21/14/14/apple-606761_1280.jpg");
		productImagesRepository.save(images4);
		log.info("ProductImages 테이블에 테이터 5 입력");

		Product product5 = new Product();
		product5.setProductID(41L);
		ProductImages images5 = new ProductImages();
		images5.setFilename("https://cdn.pixabay.com/photo/2014/02/01/17/30/apple-256268_1280.jpg");
		productImagesRepository.save(images5);
		log.info("ProductImages 테이블에 테이터 6 입력");
	}
}

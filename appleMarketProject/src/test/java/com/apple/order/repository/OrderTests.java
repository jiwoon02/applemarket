package com.apple.order.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.apple.order.domain.Order;
import com.apple.order.repository.OrderRepository;
import com.apple.product.domain.Product;
import com.apple.user.domain.User;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class OrderTests {
	
	@Setter(onMethod_ = @Autowired)
	private OrderRepository orderRepository;
	
	@Test
	public void orderInsertTest() {
		
		User user = new User();
		user.setUserNo(2L);
		
		Product product = new Product();
		product.setProductID(2L);
		product.setProductName("Apple iPhone 13");
		product.setProductPrice(20000);
		
		Order order = new Order();
		order.setProduct(product);
		order.setUser(user);
		order.setPaymentMethod("Credit Card");
		order.setPostAddress("서울특별시 강남구 테헤란로 123");
		order.setPostNumber("123456789");
		order.setRequestText("문 앞에 놓아주세요");
		
		order.newOrderId();
		
		log.info("apple_order 테이블에 데이터 1 입력");
		orderRepository.save(order);
		
		
		User user1 = new User();
		user1.setUserNo(3L);
		
		Product product1 = new Product();
		product1.setProductID(3L);
		product1.setProductName("Apple MacBook Pro");
		product1.setProductPrice(15000);
		
		Order order1 = new Order();
		order1.setProduct(product1);
		order1.setUser(user1);
		order1.setPaymentMethod("PayPal");
		order1.setPostAddress("부산광역시 해운대구 해운대해변로 321");
		order1.setPostNumber("987654321");
		order1.setRequestText("빨리 배송해주세요");
		
		log.info("apple_order 테이블에 데이터 2 입력");
		orderRepository.save(order1);
		
		User user2 = new User();
		user2.setUserNo(2L);
		
		Product product2 = new Product();
		product2.setProductID(4L);
		product2.setProductName("Apple Watch Series 7");
		product2.setProductPrice(30000);
		
		Order order2 = new Order();
		order2.setProduct(product2);
		order2.setUser(user2);
		order2.setPaymentMethod("Bank Transfer");
		order2.setPostAddress("대구광역시 중구 중앙대로 101");
		order2.setPostNumber("null");
		order2.setRequestText("포장 꼼꼼히 해주세요");

		
		log.info("apple_order 테이블에 데이터 3 입력");
		orderRepository.save(order2);
	}

}

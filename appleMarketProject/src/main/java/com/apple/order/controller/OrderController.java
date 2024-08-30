package com.apple.order.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.apple.order.domain.Order;
import com.apple.order.service.OrderService;
import com.apple.product.domain.Product;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/order/*")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	
	@Value("${portone.api.secret}")
	private String partoneApiSecret;
	
	//주문 목록 페이지
	//+페이징 처리 추가 필요
	@GetMapping("/orderList")
	public String orderList(Order order, Model model) {
		List<Order> orderList = orderService.orderList(order);
		model.addAttribute("orderList", orderList);
		
		return "order/orderList";
	}
	
	//상세 페이지 -> 주문 페이지
	@GetMapping("/insertForm")
	public String insertForm(@ModelAttribute Product product, Order order, Model model) {
		model.addAttribute(product);
		return "order/insertForm";
	}
	
	//주문 등록 처리
	@PostMapping("/orderInsert")
	public ResponseEntity<String> createOrder(@RequestBody Order order, HttpServletResponse response) throws IOException {
	    try {
	        orderService.orderInsert(order);
	        response.sendRedirect("/order/orderList");
	        return ResponseEntity.ok("Order created successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating order: " + e.getMessage());
	    }
	}
}

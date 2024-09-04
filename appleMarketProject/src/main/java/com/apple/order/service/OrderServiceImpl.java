package com.apple.order.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.apple.product.domain.Product;
import com.apple.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.apple.order.domain.Order;
import com.apple.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	@Override
	public List<Order> orderList() {
		List<Order> orderList = null;
		orderList = (List<Order>)orderRepository.orderList();
		return orderList;
	}
	
	//주문완료시 상품 상태변경 추가
	@Transactional
	@Override
	public void orderInsert(Order order) {
		Product product = order.getProduct();
		product.setStatus("판매완료");
		productRepository.save(product);
		orderRepository.save(order);
	}

	@Override
	public Order getOrderByID(String orderID) {
		// 주문 ID로 주문을 조회하고, 없으면 예외 발생
        Optional<Order> order = orderRepository.findById(orderID);
        if (order.isPresent()) {
            return order.get();
        } else {
            // 적절한 예외 처리나 기본값 반환
            throw new NoSuchElementException("No order found with ID: " + orderID);
        }
	}
	

}

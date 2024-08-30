package com.apple.order.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.apple.order.domain.Order;
import com.apple.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	
//	@Override
//	public List<Order> orderList(Order order) {
//		List<Order> orderList = null;
//		orderList = (List<Order>)orderRepository.findAll();
//		return orderList;
//	}
	
	@Override
	public List<Order> orderList(Order order) {
		List<Order> orderList = null;
		orderList = (List<Order>)orderRepository.orderList();
		return orderList;
	}

	@Override
	public void orderInsert(Order order) {
		orderRepository.save(order);
	}

	@Override
	public Order findById(String orderID) {
		// 주문 ID로 주문을 조회하고, 없으면 예외 발생
        Optional<Order> order = orderRepository.findById(orderID);
        return order.get();
	}

}

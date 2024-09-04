package com.apple.order.service;

import java.util.List;

import com.apple.order.domain.Order;

public interface OrderService {

	List<Order> orderList(Long userNo);
	void orderInsert(Order order);
	Order getOrderByID(String orderID);
}

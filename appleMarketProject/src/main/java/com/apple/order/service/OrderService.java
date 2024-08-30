package com.apple.order.service;

import java.util.List;

import com.apple.order.domain.Order;

public interface OrderService {

	List<Order> orderList(Order order);
	void orderInsert(Order order);
	Order findById(String orderID);

}

package com.apple.order.service;

import java.time.LocalDateTime;
import java.util.List;

import com.apple.order.domain.Order;

public interface OrderService {

	List<Order> orderList();
	void orderInsert(Order order);
	Order getOrderByID(String orderID);
	
	//메일전송
	void sendEmail(String to, String productName, String buyuserNickname, String buyuserPhone, String postAddress, String requestText);
}

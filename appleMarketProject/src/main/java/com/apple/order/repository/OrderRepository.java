package com.apple.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apple.order.domain.Order;

public interface OrderRepository extends JpaRepository<Order, String> {
	/* -------------------------------------------------
	 * JPQL 적용
	 * 기본형식: select 별칭 from 엔티티 이름 as 별칭
	 * -------------------------------------------------*/
	
	//유저ID별로 조회하도록 조건 추가 필요
	//주문 목록 조회
	@Query("select o from Order o")
	public List<Order> orderList();
	
	//주문 내역 상세조회
	@Query("select o from Order o where o.orderID=?1")
	public Order orderDetail(String orderID);

}

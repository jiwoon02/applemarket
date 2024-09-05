package com.apple.mypage.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.apple.order.domain.Order;
import com.apple.product.domain.OrderProductDTO;
import com.apple.product.domain.Product;



public interface MypageRepository extends JpaRepository<Order, String> {
    // userNo로 Order 목록을 찾는 메서드
    List<Order> findByUserUserNo(Long userNo);
    
    // userNo로 전체 상품을 가져오는 메서드 (모든 상품 조회) 내림차순으로 가져옴
    @Query("SELECT i FROM Product i WHERE i.user.userNo = :userNo ORDER BY i.productID DESC")
    List<Product> findAllItemsByUserNo(@Param("userNo") Long userNo);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Order o WHERE o.product.productID = :productID AND o.user.userNo = :userNo")
    void deleteOrderByUserNoAndProductId(@Param("userNo") Long userNo, @Param("productID") Long productID);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM Product i WHERE i.productID = :productID AND i.user.userNo = :userNo")
    void deleteItemByUserNoAndProductId(@Param("userNo") Long userNo, @Param("productID") Long productID);

    @Query("SELECT i FROM Product i WHERE i.productID = :productID")
    Optional<Product> findItemByProductId(@Param("productID") Long productID);
    
    // userNo를 기준으로 Usershop 삭제
    void deleteByUser_UserNo(Long userNo);
    
    //주문정보 가져올 dto
    @Query("SELECT new com.apple.product.domain.OrderProductDTO(o.orderID, o.orderRegDate, p.productID, p.productName, p.productPrice) " +
            "FROM Order o JOIN o.product p " +
            "WHERE o.user.userNo = :userNo " +
            "ORDER BY o.orderRegDate DESC")
     List<OrderProductDTO> findOrderProductInfoByUserNo(@Param("userNo") Long userNo);
}

package com.apple.usershop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.usershop.domain.WishList;

public interface UsershopWishListRepository extends JpaRepository<WishList, Long>{
	// userNo를 기준으로 wishlist를 가져오는 메서드
    List<WishList> findByUser_UserNo(Long userNo);
    
    // userNo와 productId로 WishList 엔티티를 조회하는 메서드
    Optional<WishList> findByUser_UserIDAndProduct_ProductID(String userID, Long productID);
    
    // userID와 productID로 WishList 항목 삭제
    void deleteByUserUserIDAndProductProductID(String userID, Long productID);
    
    // userNo를 기준으로 Usershop 삭제
    void deleteByUser_UserNo(Long userNo);
}

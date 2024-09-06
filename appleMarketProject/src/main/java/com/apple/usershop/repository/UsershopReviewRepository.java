package com.apple.usershop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apple.usershop.domain.ItemReview;

public interface UsershopReviewRepository extends JpaRepository<ItemReview, Long>{
	// shopId를 기준으로 ItemReview 리스트 조회
    List<ItemReview> findByUsershop_ShopId(Long shopId);
    
    // shopId를 기준으로 각 selectReview 항목의 합을 구하는 쿼리
    @Query("SELECT SUM(ir.selectReview1) FROM ItemReview ir WHERE ir.usershop.shopId = :shopId")
    Long sumSelectReview1ByShopId(@Param("shopId") Long shopId);
    
    @Query("SELECT SUM(ir.selectReview2) FROM ItemReview ir WHERE ir.usershop.shopId = :shopId")
    Long sumSelectReview2ByShopId(@Param("shopId") Long shopId);
    
    @Query("SELECT SUM(ir.selectReview3) FROM ItemReview ir WHERE ir.usershop.shopId = :shopId")
    Long sumSelectReview3ByShopId(@Param("shopId") Long shopId);
    
    @Query("SELECT SUM(ir.selectReview4) FROM ItemReview ir WHERE ir.usershop.shopId = :shopId")
    Long sumSelectReview4ByShopId(@Param("shopId") Long shopId);
    
    @Query("SELECT SUM(ir.selectReview5) FROM ItemReview ir WHERE ir.usershop.shopId = :shopId")
    Long sumSelectReview5ByShopId(@Param("shopId") Long shopId);
    
    // userNo를 기준으로 Usershop 삭제
    void deleteByUser_UserNo(Long userNo);
    
    // userNo를 기준으로 productId를 가져오는 메서드
    @Query("SELECT ir.product.productID FROM ItemReview ir WHERE ir.user.userNo = :userNo")
    List<Long> findProductIdsByUserNo(@Param("userNo") Long userNo);
    
    // userNo와 productID로 review를 찾는 메서드
    @Query("SELECT r FROM ItemReview r WHERE r.user.userNo = :userNo AND r.product.productID = :productID")
    Optional<ItemReview> findByUserNoAndProductId(@Param("userNo") Long userNo, @Param("productID") Long productID);
}

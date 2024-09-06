package com.apple.product.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.apple.product.domain.Product;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
            "JOIN p.user u " +
            "WHERE (:search = 'product_name' AND p.productName LIKE :keyword) " +
            "OR (:search = 'user_nickname' AND u.userNickname LIKE :keyword) " +
            "OR (:search = 'product_reg_date' AND p.productRegDate BETWEEN :startDate AND :endDate)")
    Page<Product> searchProducts(@Param("search") String search,
                                 @Param("keyword") String keyword,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate,
                                 Pageable pageable);

    Page<Product> findAllByOrderByProductRegDateDesc(Pageable pageable);

    Page<Product> findByCategoryCategoryID(String categoryID, Pageable pageable);

    @Query("select p from Product p where p.user.location.locationID between :startID and :endID")
    Page<Product> findByUserLocationLocationIDRange(@Param("startID") Long startID,
                                                    @Param("endID") Long endID,
                                                    Pageable pageable);
    // 특정 userNo를 가진 Test_item 리스트 조회
    List<Product> findByUser_UserNo(Long userNo);
    
    // userNo를 기준으로 productId의 개수를 가져오는 메서드
    Long countByUser_UserNo(Long userNo);

    
<<<<<<< HEAD
    @Query("SELECT p.category.categoryID AS categoryID, COUNT(p) AS count " +
            "FROM Product p GROUP BY p.category.categoryID")
     List<Object[]> countProductsByCategory();
     
=======
    @Query("SELECT COUNT(p) FROM Product p GROUP BY p.category")
	public Long findcategoryCount();

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.productStatus = :status WHERE p.productID = :productID")
    void updateProductStatus(@Param("productID") Long productID, @Param("status") String status);

    
    // userNo를 기준으로 Usershop 삭제
    void deleteByUser_UserNo(Long userNo);
>>>>>>> b3f0491cec9751fe5025a29a9c0d165174707c44
}

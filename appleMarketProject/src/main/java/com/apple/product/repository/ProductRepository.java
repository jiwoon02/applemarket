package com.apple.product.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.apple.product.domain.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

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
    Page<Product> findByUserLocationLocationIDRange(@Param("startID") long startID,
                                                    @Param("endID") long endID,
                                                    Pageable pageable);
}

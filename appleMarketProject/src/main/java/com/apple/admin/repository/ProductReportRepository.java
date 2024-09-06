package com.apple.admin.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apple.admin.domain.ProductReport;
import com.apple.product.domain.Product;

public interface ProductReportRepository extends JpaRepository<ProductReport, Long>{

	@Query("SELECT r.product, COUNT(r) FROM ProductReport r GROUP BY r.product")
	List<Object[]> findProductReportCounts();

	// 신고된 상품 조회 (ID가 아닌 Product 객체로 조회)
	List<ProductReport> findByProduct(Product product);

	Long countByProduct(Product product);

	/*
	@Query("SELECT r.productID, COUNT(r) FROM ProductReport r GROUP BY productID")
	public Map<Long, Long> ReportConut();
	public List<ProductReport> findByProductID(Product product);
	*/

	// userNo를 기준으로 Usershop 삭제
    void deleteByUser_UserNo(Long userNo);
	
	
	@Query("SELECT r.product.productID as productID, COUNT(r) AS count FROM ProductReport r GROUP BY r.product.productID")
	List<Object[]> ReportCount();
	
	
	@Modifying
    @Query("DELETE FROM ProductReport pr WHERE pr.product.productID IN :productIds")
	void deleteByProductIds(List<Long> productIds);
	
	
	

}

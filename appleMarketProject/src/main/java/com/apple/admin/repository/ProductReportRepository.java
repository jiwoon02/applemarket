package com.apple.admin.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apple.admin.domain.ProductReport;
import com.apple.product.domain.Product;

public interface ProductReportRepository extends JpaRepository<ProductReport, Long>{

	@Query("SELECT r.product, COUNT(r) FROM ProductReport r GROUP BY r.product")
	List<Object[]> findProductReportCounts();

	// 신고된 상품 조회 (ID가 아닌 Product 객체로 조회)
	List<ProductReport> findByProduct(Product product);

	Long countByProduct(Product product);
}

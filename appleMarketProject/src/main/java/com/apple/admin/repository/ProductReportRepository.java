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
	
	
	@Query("SELECT r.productID.productID as productID, COUNT(r) AS count FROM ProductReport r GROUP BY r.productID.productID")
	List<Object[]> ReportCount();
	
	
	@Modifying
    @Query("DELETE FROM ProductReport pr WHERE pr.productID.productID IN :productIds")
	void deleteByProductIds(List<Long> productIds);
	
	List<ProductReport> findByProductID(Product product);
	
	

}

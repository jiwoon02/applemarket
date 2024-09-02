package com.apple.admin.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.apple.admin.domain.ProductReport;
import com.apple.product.domain.Product;

public interface ProductReportRepository extends JpaRepository<ProductReport, Long>{
	
	@Query("SELECT r.productID, COUNT(r) FROM ProductReport r GROUP BY productID")
	public Map<Long, Long> ReportConut();
	public List<ProductReport> findByProductID(Product product);

}

package com.apple.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.apple.product.domain.Product;
import com.apple.product.domain.ProductImages;

public interface ProductImagesRepository extends JpaRepository<ProductImages, Long> {

	public void deleteByProduct(Product product);
	
	  @Query("SELECT pi FROM ProductImages pi WHERE pi.filename = :filename AND pi.product = :product")
	    ProductImages findByFilenameAndProduct(@Param("filename") String filename, @Param("product") Product product);
	}
    
    


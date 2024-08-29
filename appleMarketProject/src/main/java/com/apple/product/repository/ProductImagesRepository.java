package com.apple.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.product.domain.Product;
import com.apple.product.domain.ProductImages;

public interface ProductImagesRepository extends JpaRepository<ProductImages, Long> {

	public void deleteByProduct(Product product);

}

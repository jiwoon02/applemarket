package com.apple.product.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apple.product.domain.Product;
import com.apple.product.domain.ProductImages;

public interface ProductImagesRepository extends JpaRepository<ProductImages, Long> {

	public void deleteByProduct(Product product);
    ProductImages findByFilenameAndProduct(String filename, Product product);

}

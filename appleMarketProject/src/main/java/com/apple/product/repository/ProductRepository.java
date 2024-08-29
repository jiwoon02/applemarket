package com.apple.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.apple.product.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{
	
}

package com.apple.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apple.common.vo.PageRequestDTO;
import com.apple.common.vo.PageResponseDTO;
import com.apple.product.domain.Product;
import com.apple.product.service.ProductService;

@RestController
@RequestMapping("/api/product/*")
@CrossOrigin
@RequiredArgsConstructor
public class ProductRestController {

	private final ProductService productService;
	
	@GetMapping("/list")
	public PageResponseDTO<Product> list(PageRequestDTO pageRequestDTO) {

		return productService.list(pageRequestDTO);
	}
	
}

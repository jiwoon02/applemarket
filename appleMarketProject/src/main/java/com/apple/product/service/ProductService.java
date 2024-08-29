package com.apple.product.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.apple.common.vo.PageRequestDTO;
import com.apple.common.vo.PageResponseDTO;
import com.apple.product.domain.Product;
import com.apple.product.domain.ProductImages;

public interface ProductService {
	public List<Product> productList(Product product);
	public PageResponseDTO<Product> list(PageRequestDTO pageRequestDTO);

	public Product productDetail(Product product);
	public void productVisitCntUpdate(Product product);

	public void productInsert(Product product);
	public Product getProduct(Long productID);
	public void productUpdate(Product product);
	public Product updateForm(Product product);
	
	public void updateProductImages(Product product, List<MultipartFile> files);
	public List<ProductImages> saveProductImages(Product product, List<MultipartFile> files);
}

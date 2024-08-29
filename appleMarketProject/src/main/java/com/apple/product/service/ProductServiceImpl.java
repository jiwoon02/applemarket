package com.apple.product.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.apple.common.util.CustomeFileUtil;
import com.apple.common.vo.PageRequestDTO;
import com.apple.common.vo.PageResponseDTO;
import com.apple.product.domain.Product;
import com.apple.product.domain.ProductImages;
import com.apple.product.repository.ProductImagesRepository;
import com.apple.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;
	private final ProductImagesRepository productImagesRepository;
	private final CustomeFileUtil fileUtil;

	@Override
	public List<Product> productList(Product product) {
		return productRepository.findAll();
	}

	// 페이징 처리
	@Override
	public PageResponseDTO<Product> list(PageRequestDTO pageRequestDTO) {
		Pageable pageable = PageRequest.of(
				pageRequestDTO.getPage() - 1,
				pageRequestDTO.getSize(), 
				Sort.by("productRegDate").descending()
		);
		
		Page<Product> result = productRepository.findAll(pageable);
		List<Product> productList = result.getContent();
		long totalCount = result.getTotalElements();
		
		return PageResponseDTO.<Product>withAll()
				.dtoList(productList)
				.pageRequestDTO(pageRequestDTO)
				.totalCount(totalCount)
				.build();
	}
	
	// 조회수 증가
	@Override
	public void productVisitCntUpdate(Product product) {
		Product dataProduct = getProduct(product.getProductID());
		dataProduct.setProductVisitCount(dataProduct.getProductVisitCount() + 1);
		productRepository.save(dataProduct);
	}

	// 상품 상세 정보
	@Override
	public Product productDetail(Product product) {
		productVisitCntUpdate(product);
		Optional<Product> productOptional = productRepository.findById(product.getProductID());
		return productOptional.orElseThrow();
	}
	
	@Override
	public void productInsert(Product product) {
	    // 이미지 파일 처리
	    List<MultipartFile> files = product.getFiles();
	    if (files != null && !files.isEmpty()) {
	        List<ProductImages> productImagesList = saveProductImages(product,files);
	        product.setProductImages(productImagesList);
	    }
	    
	    productRepository.save(product);
	}

	
	// 상품 업데이트
	@Override
	public void productUpdate(Product product) {
		Product updateProduct = getProduct(product.getProductID());

		// 기존 필드 업데이트
		updateProduct.setProductName(product.getProductName());
		updateProduct.setProductPrice(product.getProductPrice());
		updateProduct.setPostPrice(product.getPostPrice());
		updateProduct.setCategory(product.getCategory());
		updateProduct.setProductDescription(product.getProductDescription());
		
		// 기존 이미지 삭제 및 새로운 이미지 추가
		updateProductImages(updateProduct, product.getFiles());
		
		productRepository.save(updateProduct);
	}
	
	@Override
	public void updateProductImages(Product product, List<MultipartFile> files) {
		if (files != null && !files.isEmpty()) {
			productImagesRepository.deleteByProduct(product); // 기존 이미지 삭제
			List<ProductImages> productImagesList = saveProductImages(product, files);
			product.setProductImages(productImagesList);
		}
	}
	
	@Override
	public List<ProductImages> saveProductImages(Product product, List<MultipartFile> files) {
		List<ProductImages> productImagesList = new ArrayList<>();
		List<String> savedFileNames = fileUtil.saveFiles(files);
		for (String fileName : savedFileNames) {
			ProductImages productImage = new ProductImages();
			productImage.setFilename(fileName);
			productImage.setProduct(product);
			productImagesList.add(productImage);
		}
		return productImagesList;
	}

	
	@Override
	public Product getProduct(Long productID) {
		Optional<Product> productOptional = productRepository.findById(productID);
		Product updateData = productOptional.orElseThrow();
		
		return updateData;
	}

	@Override
	public Product updateForm(Product product) {
		// TODO Auto-generated method stub
		return null;
	}
}

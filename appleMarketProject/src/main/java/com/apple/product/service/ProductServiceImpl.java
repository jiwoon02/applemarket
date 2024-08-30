package com.apple.product.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Transactional
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
    public Product productInsert(Product product, List<MultipartFile> files) {
        // 상품 저장
        Product savedProduct = productRepository.save(product);
        
        if (files != null && !files.isEmpty()) {
            try {
                saveProductImages(files, savedProduct);
            } catch (java.io.IOException e) {
                throw new RuntimeException("이미지 저장 중 오류 발생: " + e.getMessage());
            }
        }

        return savedProduct;
    }
    
    @Override
    public void saveProductImages(List<MultipartFile> files, Product product) throws IOException {
        List<String> originalFilenames = fileUtil.saveFiles(files, product.getProductID());
        
        List<ProductImages> images = new ArrayList<>();
        for (String originalFilename : originalFilenames) {
            ProductImages image = new ProductImages();
            image.setProduct(product);
            image.setFilename(originalFilename); // 원본 파일 이름 그대로 저장
            images.add(image);
        }
        productImagesRepository.saveAll(images);
    }



    // 상품 업데이트

    @Override
    public void productUpdate(Product product) {
        Product updateProduct = getProduct(product.getProductID());
        Long productId = updateProduct.getProductID();

        // 삭제할 이미지 처리
        List<String> existingFilenames = updateProduct.getProductImages()
                                                      .stream()
                                                      .map(ProductImages::getFilename)
                                                      .toList();

        for (String filename : existingFilenames) {
            if (!product.getFiles().stream()
                        .map(MultipartFile::getOriginalFilename)
                        .toList().contains(filename)) {
                ProductImages image = productImagesRepository.findByFilenameAndProduct(filename, updateProduct);
                if (image != null) {
                    fileUtil.deleteFile(filename, productId); // 상품별 폴더에서 파일 삭제
                    productImagesRepository.delete(image);
                    updateProduct.getProductImages().remove(image);
                }
            }
        }

        // 새로운 이미지 추가
        List<MultipartFile> files = product.getFiles();
        if (files != null && !files.isEmpty()) {
        	try {
        		// 매개변수 순서 수정: files가 첫 번째, product가 두 번째로 전달되어야 함
        		saveProductImages(files, updateProduct); 
				
			} catch (IOException e) {
				throw new RuntimeException("이미지 저장 중 오류 발생: " + e.getMessage(), e);
			}
        }

        // 나머지 필드 업데이트
        updateProduct.setProductName(product.getProductName());
        updateProduct.setProductPrice(product.getProductPrice());
        updateProduct.setPostPrice(product.getPostPrice());
        updateProduct.setCategory(product.getCategory());
        updateProduct.setProductDescription(product.getProductDescription());

        productRepository.save(updateProduct);
    }


    @Override
    public Product getProduct(Long productID) {
        Optional<Product> productOptional = productRepository.findById(productID);
        return productOptional.orElseThrow();
    }
    
    @Override
    public void deleteProduct(Long productID) {
        Product product = getProduct(productID);

        if (product.getProductImages() != null) {
            for (ProductImages image : product.getProductImages()) {
                fileUtil.deleteFile(image.getFilename(), productID); // 상품별 폴더에서 파일 삭제
            }
        }

        productImagesRepository.deleteByProduct(product);
        productRepository.delete(product);

        // 상품 폴더 삭제
        fileUtil.deleteProductFolder(productID);
    }

}

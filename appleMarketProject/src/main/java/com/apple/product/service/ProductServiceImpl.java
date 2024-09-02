package com.apple.product.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.apple.location.repository.LocationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImagesRepository productImagesRepository;
    private final CustomeFileUtil fileUtil;
    private final LocationRepository locationRepository;

    /*
        @Override
        public List<Product> productList(Product product) {
            return productRepository.findAll();
        }
    */
    @Override
    public PageResponseDTO<Product> list(PageRequestDTO pageRequestDTO) {
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        String keyword = pageRequestDTO.getKeyword();
        String search = pageRequestDTO.getSearch();

        log.info("Received keyword: {}", keyword);

        if (keyword == null || keyword.isEmpty()) {
            log.info("키워드 없이 기본값. 최신날짜순 정렬");
            Page<Product> result = productRepository.findAllByOrderByProductRegDateDesc(pageRequest);
            return new PageResponseDTO<>(result.getContent(), pageRequestDTO, result.getTotalElements());
        } else {
            if (keyword.startsWith("@")) {
                search = "user_nickname";
                keyword = keyword.substring(1);  // '@' 제거
            } else {
                search = "product_name";  // 기본 검색 필드로 설정
            }

            log.info("Searching with field: {} and keyword: {}", search, keyword);

            Page<Product> result = productRepository.searchProducts(
                    search,
                    "%" + keyword + "%",  // 와일드카드 추가
                    pageRequestDTO.getStartDate(),
                    pageRequestDTO.getEndDate(),
                    pageRequest
            );

            return new PageResponseDTO<>(result.getContent(), pageRequestDTO, result.getTotalElements());
        }
    }


    @Override
    public PageResponseDTO<Product> getProductByLocationIDRange(long locationID, PageRequestDTO pageRequestDTO){
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        long startID = locationID -3;
        long endID = locationID + 3;

        if(!locationExists(startID)){
            startID = locationID;
        }
        if(!locationExists(endID)){
            endID = locationID;
        }

        Page<Product> result = productRepository.findByUserLocationLocationIDRange(startID, endID, pageRequest);
        return new PageResponseDTO<>(result.getContent(), pageRequestDTO, result.getTotalElements());
    }

    public boolean locationExists(long locationID){
        return locationRepository.existsById(locationID);
    }

    @Override
    public PageResponseDTO<Product> getProductsByCategory(String categoryID, PageRequestDTO pageRequestDTO) {
        PageRequest pageRequest = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        Page<Product> result = productRepository.findByCategoryCategoryID(categoryID, pageRequest);
        return new PageResponseDTO<>(result.getContent(), pageRequestDTO, result.getTotalElements());
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
        log.info("file cnt :" + files.size());
        for(MultipartFile file : files) {
            log.info("file name :" + file.getOriginalFilename());
            log.info("file content :" + file.getContentType());
            log.info("file getName" + file.getName());
        }
        // 상품 저장
        Product savedProduct = productRepository.save(product);

        if (files != null && !files.isEmpty()) {
            try {
                saveProductImages(files, savedProduct); //insert
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
    public void productUpdate(Product product, List<MultipartFile> files) {
        Product updateProduct = getProduct(product.getProductID());
        Long productId = updateProduct.getProductID();

        log.info("file cnt :" + files.size());
        for(MultipartFile file : files) {
            log.info("file name :" + file.getOriginalFilename());
            log.info("file content :" + file.getContentType());
            log.info("file getName" + file.getName());
        }
        List<ProductImages> selectedImages = productImagesRepository.findByFilenameAndProduct(product);
        log.info("selectedImages =>" + selectedImages.size());

        List<String> existingFilenames = product.getExistingFileNames();
        log.info("existingFilenames =>" + existingFilenames.toString());

        for (ProductImages image : selectedImages) {
            log.info("existingFilenames =>" + existingFilenames.toString());
            log.info("image.getFilename() =>" + image.getFilename().toString());

            if (!existingFilenames.contains(image.getFilename())) {
                log.info("point delete" + image.getFilename());
                fileUtil.deleteFile(image.getFilename(), productId); // 상품별 폴더에서 파일 삭제
                productImagesRepository.delete(image);
                updateProduct.getProductImages().remove(image);
            }

        }

        if (files != null && !files.isEmpty()) {
            try {
                log.info("saveFiles");
                saveProductImages(files, product); //insert
            } catch (java.io.IOException e) {
                throw new RuntimeException("이미지 저장 중 오류 발생: " + e.getMessage());
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

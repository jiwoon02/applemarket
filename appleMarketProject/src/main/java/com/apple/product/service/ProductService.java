
package com.apple.product.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import com.apple.common.vo.PageRequestDTO;
import com.apple.common.vo.PageResponseDTO;
import com.apple.product.domain.Product;
import com.apple.product.domain.ProductImages;

public interface ProductService {

	//List<Product> productList(Product product);

    PageResponseDTO<Product> list(PageRequestDTO pageRequestDTO);

    void productVisitCntUpdate(Product product);

    Product productDetail(Product product);

    public Product productInsert(String token, Product product, List<MultipartFile> files);

    public void saveProductImages(List<MultipartFile> files, Product product) throws IOException;

    void productUpdate(Product product, List<MultipartFile> files);

    Product getProduct(Long productID);
    
    void deleteProduct(Long productID);

    PageResponseDTO<Product> getProductsByCategory(String categoryID, PageRequestDTO pageRequestDTO);

    public PageResponseDTO<Product> getProductByLocationIDRange(Long locationID, PageRequestDTO pageRequestDTO);
}

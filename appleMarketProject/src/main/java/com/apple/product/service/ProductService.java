
package com.apple.product.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.apple.user.domain.User;
import com.apple.usershop.domain.WishList;

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

	String getUserIDByProductID(Long productID);
    public PageResponseDTO<Product> getProductByLocationIDRange(Long locationID, PageRequestDTO pageRequestDTO);

    public Long getReportCountByProductID(Long productID);

    public void reportProduct(Long productID, String reportContent, User user);
    
    public Optional<WishList> getWishListByUserIDAndProductId(String userID, Long productID);
    
    public void addWishList(String userID, Long productID);
    
    public void removeWishList(String userID, Long productID);
}

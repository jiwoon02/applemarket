package com.apple.product.controller;

import com.apple.config.SecurityConfig;
import com.apple.jwt.JwtUtil;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.apple.common.util.CustomeFileUtil;
import com.apple.common.vo.PageRequestDTO;
import com.apple.common.vo.PageResponseDTO;
import com.apple.product.domain.Product;
import com.apple.product.repository.ProductImagesRepository;
import com.apple.product.service.ProductService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;
    private final CustomeFileUtil fileUtil;
    private final ProductImagesRepository productImagesRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserService userService;

    // 페이징처리한 리스트 한페이지당 12개
    @GetMapping("/")
    public String productList( @CookieValue(value = "JWT", required = false) String token,
                               PageRequestDTO pageRequestDTO,
                               @RequestParam(value = "category", required = false) String categoryID,
                               @RequestParam(value = "locationID", required = false) Long locationID,
                               Model model) {
        String userID = null;
        if (token != null) {
            userID = jwtUtil.getUserID(token);
        }

        PageResponseDTO<Product> productList;

        if(categoryID != null) {
            productList = productService.getProductsByCategory(categoryID, pageRequestDTO);
        }else if(locationID != null) {
            productList = productService.getProductByLocationIDRange(locationID, pageRequestDTO);
        } else{
            productList = productService.list(pageRequestDTO);
        }

        model.addAttribute("productList", productList);
        return "product/productList";
    }

    @GetMapping("/product/{productID}")
    public String productDetail(@CookieValue(value = "JWT", required = false) String token, @PathVariable Long productID, Product product, Model model) {
        String currentUserID = null;

        if(token != null) {
            currentUserID = jwtUtil.getUserID(token);
            log.info("현재 토큰 유저 번호==>" + currentUserID);
        }

        product.setProductID(productID);
        Product detail = productService.productDetail(product);
        model.addAttribute("detail", detail);
        model.addAttribute("currentUserID", currentUserID);

        String newLine = System.getProperty("line.separator").toString();
        model.addAttribute("newLine", newLine);

        return "product/productDetail";
    }

    @ResponseBody
    @GetMapping("/product/view/{productID}/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable Long productID, @PathVariable String fileName) {
        // 파일 경로를 동적으로 구성
        String productFolderPath = fileUtil.createProductFolder(productID);
        String completeFilePath = Paths.get(productFolderPath, fileName).toString();

        return fileUtil.getFile(productID, fileName);
    }


    @GetMapping("/product/insertForm")
    public String productInsertForm(Product product) {
        return "product/insertForm";
    }

    @PostMapping("/product/productInsert")
    public String productInsert(@CookieValue(value = "JWT", required = false) String token, Product product, List<MultipartFile> files) throws IOException {

        if(token == null) {
            throw new IllegalArgumentException("토큰이 존재하지 않습니다.");
        }

        // 이미지 저장 시 발생할 수 있는 IOException을 처리하기 위해 try-catch 사용
        // 상품을 먼저 저장
        productService.productInsert(token, product, files);
        return "redirect:/";
    }

    @PostMapping("/product/updateForm")
    public String updateForm(Product product, Model model) {
        Product updateData = productService.getProduct(product.getProductID());
        model.addAttribute("updateData", updateData);
        return "product/updateForm";
    }

    @PostMapping("/product/productUpdate")
    public String productUpdate(Product product, List<MultipartFile> files) {
        // 서비스 호출하여 업데이트 진행
        productService.productUpdate(product,files);

        // 수정된 상품 상세 페이지로 리다이렉트
        return "redirect:/product/" + product.getProductID();
    }

    @PostMapping("/product/productDelete")
    public String productDelete(Long productID) {
        // 서비스 계층에서 실제 삭제 처리
        productService.deleteProduct(productID);

        // 삭제 후 목록 페이지로 리다이렉트
        return "redirect:/";
    }


}
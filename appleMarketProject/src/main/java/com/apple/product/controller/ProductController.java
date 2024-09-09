package com.apple.product.controller;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.apple.common.util.CustomeFileUtil;
import com.apple.common.vo.PageRequestDTO;
import com.apple.common.vo.PageResponseDTO;
import com.apple.jwt.JwtUtil;
import com.apple.product.domain.Product;
import com.apple.product.repository.ProductImagesRepository;
import com.apple.product.service.ProductService;
import com.apple.user.domain.User;
import com.apple.user.repository.UserRepository;
import com.apple.user.service.UserService;
import com.apple.usershop.domain.WishList;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        
        // 찜목록 가져오기(찜 했는지 판단을 위해)
        Optional<WishList> wishListOptional = productService.getWishListByUserIDAndProductId(currentUserID, productID);
        if (wishListOptional.isPresent()) {
        	WishList wishList = wishListOptional.get();
            model.addAttribute("wishList", wishList);
        }

        return "product/productDetail";
    }
    
    @PostMapping("/wishlist/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToWishList(@CookieValue(value = "JWT", required = false) String token, @RequestBody Map<String, Object> requestData) {
        String currentUserID = null;
        Map<String, Object> response = new HashMap<>();
        
        if (requestData.containsKey("productID")) {
            Long productID = Long.parseLong(requestData.get("productID").toString());
            
            if (token != null) {
                currentUserID = jwtUtil.getUserID(token);
                log.info("현재 토큰 유저 번호==>" + currentUserID);
            }
            
            // 위시리스트 추가 로직
            productService.addWishList(currentUserID, productID);
            
            // 필요한 경우 추가적인 정보를 응답에 포함
            response.put("status", "success");
            response.put("message", "찜목록에 추가되었습니다.");
            
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "productID가 제공되지 않았습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/wishlist/delete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteWishList(@CookieValue(value = "JWT", required = false) String token, @RequestBody Map<String, Object> requestData) {
        String currentUserID = null;
        Map<String, Object> response = new HashMap<>();
        
        if (requestData.containsKey("productID")) {
            Long productID = Long.parseLong(requestData.get("productID").toString());
            
            if (token != null) {
                currentUserID = jwtUtil.getUserID(token);
                log.info("현재 토큰 유저 번호==>" + currentUserID);
            }
            
            // 위시리스트 삭제 로직
            productService.removeWishList(currentUserID, productID);
            
            response.put("status", "success");
            response.put("message", "찜목록에서 제거되었습니다.");
            
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "productID가 제공되지 않았습니다.");
            return ResponseEntity.badRequest().body(response);
        }
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

    @PostMapping("/product/productReportInsert")
    public String reportProduct(@RequestParam Long productID, @RequestParam String reportContent, @RequestParam Long userNo) {
        // userNo로 유저를 조회
        User user = userRepository.findByUserNo(userNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        // 신고 처리
        productService.reportProduct(productID, reportContent, user);

        return "redirect:/product/" + productID;
    }


}
package com.apple.product.controller;

<<<<<<< HEAD
=======
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> feature/jiwoon
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.PathVariable;
>>>>>>> feature/jiwoon
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.apple.common.util.CustomeFileUtil;
import com.apple.common.vo.PageRequestDTO;
import com.apple.common.vo.PageResponseDTO;
import com.apple.jwt.JwtUtil;
import com.apple.product.domain.Product;
import com.apple.product.repository.ProductImagesRepository;
import com.apple.product.service.ProductService;
<<<<<<< HEAD
=======

import jakarta.servlet.http.HttpSession;

import com.apple.common.util.CustomeFileUtil;

>>>>>>> feature/jiwoon
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/product/*")
@RequiredArgsConstructor
public class ProductController {
<<<<<<< HEAD
=======
	
	private final ProductService productService;
	private final CustomeFileUtil fileUtil;
	private final JwtUtil jwtUtil;
	
	//페이징처리한 리스트 한페이지당 12개
	@GetMapping("/productList")
	public String productList(Product product, PageRequestDTO pageRequestDTO, Model model) {
		PageResponseDTO<Product> productList = productService.list(pageRequestDTO);
		model.addAttribute("productList", productList);
		return "product/productList";
	}
	
	@GetMapping("/{productID}")
	public String productDetail(@PathVariable Long productID, Product product, Model model) {
		product.setProductID(productID);
		Product detail = productService.productDetail(product);
		model.addAttribute("detail", detail);
		
		String newLine = System.getProperty("line.separator").toString();
		model.addAttribute("newLine", newLine);
		
		return "product/productDetail";
	}
	
	@ResponseBody
	@GetMapping("/view/{fileName}")
	public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
		return fileUtil.getFile(fileName);
	}
	
	@GetMapping("/insertForm")
	public String productInsertForm(Product product) {
		return "product/insertForm";
	}
>>>>>>> feature/jiwoon

    private final ProductService productService;
    private final CustomeFileUtil fileUtil;
    private final ProductImagesRepository productImagesRepository;

    // 페이징처리한 리스트 한페이지당 12개
    @GetMapping("/productList")
    public String productList(Product product, PageRequestDTO pageRequestDTO, Model model) {
        PageResponseDTO<Product> productList = productService.list(pageRequestDTO);
        model.addAttribute("productList", productList);
        return "product/productList";
    }

<<<<<<< HEAD
    @GetMapping("/{productID}")
    public String productDetail(@PathVariable Long productID, Product product, Model model) {
        product.setProductID(productID);
        Product detail = productService.productDetail(product);
        model.addAttribute("detail", detail);

        String newLine = System.getProperty("line.separator").toString();
        model.addAttribute("newLine", newLine);

        return "product/productDetail";
    }

    @ResponseBody
    @GetMapping("/view/{productID}/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable Long productID, @PathVariable String fileName) {
        // 파일 경로를 동적으로 구성
        String productFolderPath = fileUtil.createProductFolder(productID);
        String completeFilePath = Paths.get(productFolderPath, fileName).toString();

        return fileUtil.getFile(productID, fileName);
    }


    @GetMapping("/insertForm")
    public String productInsertForm(Product product) {
        return "product/insertForm";
    }

    @PostMapping("/productInsert")
    public String productInsert(Product product, List<MultipartFile> files) throws IOException {

        
        // 이미지 저장 시 발생할 수 있는 IOException을 처리하기 위해 try-catch 사용
        // 상품을 먼저 저장
        productService.productInsert(product, files);
        return "redirect:/product/productList";
    }

    @PostMapping("/updateForm")
    public String updateForm(Product product, Model model) {
        Product updateData = productService.getProduct(product.getProductID());
        model.addAttribute("updateData", updateData);
        return "product/updateForm";
    }

    @PostMapping("/productUpdate")
    public String productUpdate(Product product, List<MultipartFile> files) {
        // 서비스 호출하여 업데이트 진행
        productService.productUpdate(product,files);

        // 수정된 상품 상세 페이지로 리다이렉트
        return "redirect:/product/" + product.getProductID();
    }

    @PostMapping("/productDelete")
    public String productDelete(Long productID) {
        // 서비스 계층에서 실제 삭제 처리
        productService.deleteProduct(productID);

        // 삭제 후 목록 페이지로 리다이렉트
        return "redirect:/product/productList";
    }
=======
>>>>>>> feature/jiwoon
}

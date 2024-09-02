package com.apple.product.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.apple.common.vo.PageRequestDTO;
import com.apple.common.vo.PageResponseDTO;
import com.apple.product.domain.Product;
import com.apple.product.domain.ProductImages;
import com.apple.product.service.ProductService;

import jakarta.servlet.http.HttpSession;

import com.apple.common.util.CustomeFileUtil;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/product/*")
@RequiredArgsConstructor
public class ProductController {
	
	private final ProductService productService;
	private final CustomeFileUtil fileUtil;
	
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

	@PostMapping(value ="/productInsert")
	public String productInsert(Product product) {
		 productService.productInsert(product);
		    return "redirect:/product/productList";
	}
	
	@PostMapping("/updateForm")
	public String updateForm(Product product, Model model) {
		Product updateData = productService.getProduct(product.getProductID());
		model.addAttribute("updateData", updateData);
		
		return "product/updateForm";
	}
	

	@PostMapping("/productUpdate")
	public String productUpdate(Product product) {
		productService.productUpdate(product);
		return "redirect:/product/" + product.getProductID();
	}

}

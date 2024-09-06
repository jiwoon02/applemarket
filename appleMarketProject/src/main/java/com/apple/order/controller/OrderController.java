package com.apple.order.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apple.common.util.CustomeFileUtil;
import com.apple.order.domain.Order;
import com.apple.order.service.OrderService;
import com.apple.product.domain.Product;
import com.apple.product.service.ProductService;
import com.apple.user.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/order/*")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
	
	private final OrderService orderService;
	private final CustomeFileUtil fileUtil;
	
	private final ProductService productService;
	private final UserService userService;
	
	@Value("${portone.api.secret}")
	private String partoneApiSecret;
	
	//주문 목록 페이지
	@GetMapping("/orderList")
	public String orderList(@CookieValue(value="JWT", required=false) String token, Model model) {
		Long userNo = userService.getUserNo(token);		// 현재 로그인한 사용자의 userNo 가져오기
		List<Order> orderList = orderService.orderList(userNo); // 주문 목록 가져오기
		Map<String, String> imageFileNames = new HashMap<>();
		
		
		for (Order order : orderList) {
			Product product = order.getProduct();
			String imageFileName = product.getProductImages().isEmpty() ? "default.jpg" : product.getProductImages().get(0).getFilename();
			imageFileNames.put(order.getOrderID(), imageFileName);
		}
		
		model.addAttribute("orderList", orderList);
		model.addAttribute("imageFileNames", imageFileNames); // 각 주문의 상품 이미지 파일명 매핑
		
		return "order/orderList";
	}


	
	//상세 페이지 -> 주문 페이지
	@GetMapping("/insertForm")
	public String insertForm(@RequestParam Long productID, @CookieValue(value="JWT", required=false) String token,Model model) {
		
		Long userNo = userService.getUserNo(token);		// 현재 로그인한 사용자의 userNo 가져오기
		log.info("userNo: "+userNo.toString());

		String userName = userService.getNameByUserNo(userNo);  //사용자 이름
		String userPhone = userService.getPhoneByUserNo(userNo);	//사용자 전화번호
		//상품 정보, 상품 이미지 전달
		Product product = productService.getProduct(productID);
	    if (product == null) {
	        return null; 
	    }
	    String imageFileName = product.getProductImages().isEmpty() ? null : product.getProductImages().get(0).getFilename();
	    Long imageFileID = product.getProductImages().isEmpty() ? null : product.getProductImages().get(0).getProductImageID();
	    
	    log.info("imageFileID: "+imageFileID.toString());
	    
	    model.addAttribute("product", product);
	    model.addAttribute("imageFileName", imageFileName);
	    model.addAttribute("productImageID", imageFileID);
	    
	    //사용자 정보 전달
	    model.addAttribute("userName", userName);
	    model.addAttribute("userPhone", userPhone);
	    model.addAttribute("userNo", userNo);
	    
	    return "order/insertForm";
	}
	
	
	//주문 등록 처리
	@PostMapping("/orderInsert")
	public ResponseEntity<String> createOrder(@RequestBody Order order, HttpServletResponse response) throws IOException {
	    try {
	        orderService.orderInsert(order);
	        return ResponseEntity.ok("Order created successfully");
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error creating order: " + e.getMessage());
	    }
	}
	
	
	
    // 주문 상세 페이지
    @GetMapping("/{orderID}")
    public String orderDetail(@PathVariable String orderID, Model model) {
    	
    	Order order = orderService.getOrderByID(orderID);

    	//상품 정보, 상품 이미지 전달
		Product product = productService.getProduct(order.getProduct().getProductID());
	    if (product == null) {
	        return "redirect:/error"; 
	    }
	    
	    log.info("상품 이미지: "+product.getProductImages().get(0).getFilename());
	    
	 // product 또는 getProductImages()가 null일 경우 처리
	    String imageFileName = null;
	    if (product != null && !product.getProductImages().isEmpty()) {
	        imageFileName = product.getProductImages().get(0).getFilename(); // 첫 번째 이미지의 파일 이름 가져오기
	        log.info("imageFileName값: "+imageFileName);
	    }
        model.addAttribute("order", order);
        model.addAttribute("imageFileName", imageFileName);
        return "order/orderDetail";
    }
   
    //+상품 이미지 출력
    @GetMapping("view/product_{productID}/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> viewFileGET(@PathVariable Long productID, @PathVariable String fileName){
    	log.info("filename: " + fileName);
    	log.info("productId: " + productID.toString());
    	return fileUtil.getFile(productID, fileName);
    }
}

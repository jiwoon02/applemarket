package com.apple.chat.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apple.product.service.ProductService;
import com.apple.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@CrossOrigin
@RequestMapping("/chatroom/*")
@RequiredArgsConstructor
public class ChatController {

	@Value("${sendbird.appId}")
	private String appId;
	
	private final UserService userService;
	private final ProductService productService;
	
	//구매자, 판매자 ID를 JSON 형태로 전송
	@GetMapping("/chatdata")
	@ResponseBody
	public Map<String, String> chatdata( @RequestParam Long productID, @CookieValue(value="JWT", required=false) String token ) throws Exception{
		
		Long buyerNo = userService.getUserNo(token);		// 현재 로그인한 사용자의 userNo 가져오기
		String buyerID = userService.getUserIDByUserNo(buyerNo);	//userNo로 현재 로그인한 사용자(=구매자)ID 가져오기
		String buyerNickname = userService.getUserNicknameByUserNo(buyerNo);	//userNo로 현재 로그인한 사용자(=구매자)ID 가져오기
	
		String sellerID = productService.getUserIDByProductID(productID);//productID로 판매자의 userID를 가져오기
		String sellerNickname = productService.getUserNicknameByProductID(productID);//productID로 판매자의 userNickname을 가져오기
				
		Map<String, String> chatMap = new HashMap<>();	//JSON 데이터 전달을 위한 MAP 생성
		chatMap.put("appId", appId);
		chatMap.put("buyerId", buyerID);	
		chatMap.put("buyerNickname", buyerNickname);
		chatMap.put("sellerId", sellerID);	
		chatMap.put("sellerNickname", sellerNickname);
		return chatMap;
	}
	
	//본인의 ID, 닉네임을 JSON 형태로 전송
	@GetMapping("/chatdataList")
	@ResponseBody
	public Map<String, String> chatdataList( @CookieValue(value="JWT", required=false) String token ) throws Exception{
		
		Long userNo = userService.getUserNo(token);		// 현재 로그인한 사용자의 userNo 가져오기
		String userID = userService.getUserIDByUserNo(userNo);	//userNo로 현재 로그인한 사용자(=구매자)ID 가져오기
		String userNickname = userService.getUserNicknameByUserNo(userNo);	//userNo로 현재 로그인한 사용자(=구매자)ID 가져오기
		
		Map<String, String> chatMap = new HashMap<>();	//JSON 데이터 전달을 위한 MAP 생성
		chatMap.put("appId", appId);
		chatMap.put("buyerId", userID);	
		chatMap.put("buyerNickname", userNickname);
		return chatMap;
	}
	
	//상품 상세페이지에서 채팅하기 버튼 클릭 시 채팅 페이지로 이동
	@GetMapping("/chatroom")
	public String chatroom(@RequestParam Long productID, Model model) {
		model.addAttribute("productID", productID);
		return "chatroom/chatroom";
	}
	
	//메인에서 채팅하기 버튼 클릭 시 채팅 내역으로 이동
	@GetMapping("/chatroomList")
	public String chatroomList() {
		return "chatroom/chatroomList";
	}
	
}
